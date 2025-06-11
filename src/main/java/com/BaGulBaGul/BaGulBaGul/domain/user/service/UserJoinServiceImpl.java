package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.SocialLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.alarm.UserAlarmStatus;
import com.BaGulBaGul.BaGulBaGul.domain.alarm.repository.UserAlarmStatusRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.SocialLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.UserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtProvider;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.DuplicateUsernameException;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.UserNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.SocialLoginUserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.global.auth.oauth2.dto.OAuth2JoinTokenSubject;
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
    private final UserAlarmStatusRepository userAlarmStatusRepository;

    private final UserImageService userImageService;

    @Override
    @Transactional
    public SocialLoginUser registerSocialLoginUser(SocialLoginUserJoinRequest socialLoginUserJoinRequest) {
        String joinToken = socialLoginUserJoinRequest.getJoinToken();
        //joinToken에서 OAuth2JoinTokenSubject 추출.
        OAuth2JoinTokenSubject oAuth2JoinTokenSubject = jwtProvider.getOAuth2JoinTokenSubject(joinToken);
        //유저 생성
        User user = registerUser(socialLoginUserJoinRequest.getUserRegisterRequest());
        //소셜 유저 생성
        SocialLoginUser socialLoginUser = SocialLoginUser.builder()
                .id(oAuth2JoinTokenSubject.getSocialLoginId())
                .provider(oAuth2JoinTokenSubject.getOAuth2Provider())
                .user(user)
                .build();
        socialLoginUserRepository.save(socialLoginUser);
        return socialLoginUser;
    }

    @Override
    @Transactional
    public User registerUser(UserRegisterRequest userRegisterRequest) {
        User user = User.builder()
                .nickName(userRegisterRequest.getNickname())
                .email(userRegisterRequest.getEmail())
                .build();
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            if(DuplicateUsernameException.check(e)) {
                throw new DuplicateUsernameException();
            }
        }
        userAlarmStatusRepository.save(
                UserAlarmStatus.builder()
                        .user(user)
                        .totalAlarmCount(0L)
                        .uncheckedAlarmCount(0L)
                        .build()
        );
        return user;
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        //유저 이미지 삭제
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        userImageService.setImage(user, null);
        //소셜로그인 정보 삭제
        socialLoginUserRepository.deleteByUserId(userId);
        //유저 정보 삭제
        userRepository.deleteById(userId);
    }

    @Override
    public boolean checkDuplicateUsername(String username) {
        return userRepository.existsByNicknameIgnoreCase(username);
    }
}
