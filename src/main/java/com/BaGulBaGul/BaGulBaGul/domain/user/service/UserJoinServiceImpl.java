package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.alarm.UserAlarmStatus;
import com.BaGulBaGul.BaGulBaGul.domain.alarm.repository.UserAlarmStatusRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.AdminManageEventHostUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.AdminManagePasswordLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.PasswordLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.SocialLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.AdminManageEventHostUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.AdminManagePasswordLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.SocialLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.DuplicateUsernameException;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.UserNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.AdminManageEventHostUserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.SocialLoginUserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserJoinServiceImpl implements UserJoinService {

    private final JwtProvider jwtProvider;
    private final SocialLoginUserRepository socialLoginUserRepository;
    private final UserRepository userRepository;
    private final AdminManageEventHostUserRepository adminManageEventHostUserRepository;
    private final UserAlarmStatusRepository userAlarmStatusRepository;

    private final UserImageService userImageService;
    private final UserRoleService userRoleService;
    private final PasswordLoginUserService passwordLoginUserService;
    private final AdminManagePasswordLoginUserService adminManagePasswordLoginUserService;
    private final SocialLoginUserService socialLoginUserService;
    private final AdminManageEventHostUserService adminManageEventHostUserService;

    @Override
    @Transactional
    public SocialLoginUser joinSocialLoginUser(SocialLoginUserJoinRequest socialLoginUserJoinRequest) {
        String joinToken = socialLoginUserJoinRequest.getJoinToken();
        User user = registerUser(socialLoginUserJoinRequest.getUserRegisterRequest());
        SocialLoginUser socialLoginUser = socialLoginUserService.registerSocialLoginUser(user, joinToken);
        return socialLoginUser;
    }

    @Override
    @Transactional
    public AdminManageEventHostUser joinAdminManageEventHostUser(
            AdminManageEventHostUserJoinRequest eventHostUserRegisterRequest
    ) {
        User user = registerUser(eventHostUserRegisterRequest.getUserRegisterRequest());
        AdminManageEventHostUser adminManageEventHostUser = adminManageEventHostUserRepository.save(
                AdminManageEventHostUser.builder()
                        .user(user)
                        .build()
        );
        return adminManageEventHostUser;
    }

    @Override
    public AdminManagePasswordLoginUser joinAdminManagePasswordLoginUser(
            AdminManagePasswordLoginUserJoinRequest adminManagePasswordLoginUserJoinRequest) {
        User user = registerUser(adminManagePasswordLoginUserJoinRequest.getUserRegisterRequest());
        PasswordLoginUser passwordLoginUser = passwordLoginUserService.registerPasswordLoginUser(
                adminManagePasswordLoginUserJoinRequest.getPasswordLoginUserRegisterRequest(),
                user
        );
        AdminManagePasswordLoginUser adminManagePasswordLoginUser = adminManagePasswordLoginUserService
                .registerAdminManagePasswordLoginUser(passwordLoginUser);
        return adminManagePasswordLoginUser;
    }

    @Override
    @Transactional
    public User registerUser(UserRegisterRequest userRegisterRequest) {
        User user = User.builder()
                .nickName(userRegisterRequest.getNickname())
                .email(userRegisterRequest.getEmail())
                .build();
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            if(DuplicateUsernameException.check(e)) {
                throw new DuplicateUsernameException();
            }
        }
        //알람 테이블 설정
        userAlarmStatusRepository.save(
                UserAlarmStatus.builder()
                        .user(user)
                        .totalAlarmCount(0L)
                        .uncheckedAlarmCount(0L)
                        .build()
        );
        //역할 설정
        if(userRegisterRequest.getRoles() != null) {
            userRoleService.addRoles(user.getId(), userRegisterRequest.getRoles());
        }
        return user;
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        //유저 이미지 삭제
        userImageService.setImage(user, null);
        //소셜로그인 정보 삭제
        SocialLoginUser socialLoginUser = user.getSocialLoginUser();
        if(socialLoginUser != null) {
            socialLoginUserService.deRegisterSocialLoginUser(socialLoginUser.getId());
        }
        //password login user 정보 삭제
        PasswordLoginUser passwordLoginUser = user.getPasswordLoginUser();
        if(passwordLoginUser != null) {
            //AdminManagePasswordLoginUser 정보 삭제
            AdminManagePasswordLoginUser ampwUser = passwordLoginUser.getAdminManagePasswordLoginUser();
            if(ampwUser != null) {
                adminManagePasswordLoginUserService.deRegisterAdminManagePasswordLoginUser(ampwUser.getId());
            }
            //PasswordLoginUser 정보 삭제
            passwordLoginUserService.deRegisterPasswordLoginUser(passwordLoginUser.getLoginId());
        }
        //AdminManageEventHostUser 정보 삭제
        AdminManageEventHostUser adminManageEventHostUser = user.getAdminManageEventHostUser();
        if(adminManageEventHostUser != null) {
            adminManageEventHostUserService.deRegisterAdminManageEventHostUser(adminManageEventHostUser.getId());
        }

        //역할 삭제는 on delete cascade
        //유저 정보 삭제
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public void deleteAdminManageEventHostUser(Long adminManageEventHostUserId) {
        AdminManageEventHostUser adminManageEventHostUser = adminManageEventHostUserRepository
                .findById(adminManageEventHostUserId).orElseThrow(UserNotFoundException::new);
        deleteUser(adminManageEventHostUser.getUser().getId());
    }

    @Override
    public void deleteAdminManagePasswordLoginUserByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        PasswordLoginUser passwordLoginUser = user.getPasswordLoginUser();
        AdminManagePasswordLoginUser adminManagePasswordLoginUser = passwordLoginUser
                .getAdminManagePasswordLoginUser();
        if(adminManagePasswordLoginUser == null) {

        }
    }


    @Override
    public boolean checkDuplicateUsername(String username) {
        return userRepository.existsByNicknameIgnoreCase(username);
    }
}
