package com.BaGulBaGul.BaGulBaGul.domain.user.info.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.SocialLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.auth.service.JwtProvider;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.SocialLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.UserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.exception.UserNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.repository.SocialLoginUserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.auth.oauth2.dto.OAuth2JoinTokenSubject;
import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import com.BaGulBaGul.BaGulBaGul.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserJoinServiceImpl implements UserJoinService {

    private final JwtProvider jwtProvider;
    private final SocialLoginUserRepository socialLoginUserRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void registerSocialLoginUser(SocialLoginUserJoinRequest socialLoginUserJoinRequest) {
        String joinToken = socialLoginUserJoinRequest.getJoinToken();
        //joinToken에서 OAuth2JoinTokenSubject 추출.
        OAuth2JoinTokenSubject oAuth2JoinTokenSubject = jwtProvider.getOAuth2JoinTokenSubject(joinToken);
        //유저 생성
        User user = registerUser(socialLoginUserJoinRequest.toUserRegisterRequest());
        //소셜 유저 생성
        SocialLoginUser socialLoginUser = SocialLoginUser.builder()
                .id(oAuth2JoinTokenSubject.getSocialLoginId())
                .provider(oAuth2JoinTokenSubject.getOAuth2Provider())
                .user(user)
                .build();
        socialLoginUserRepository.save(socialLoginUser);
    }

    @Override
    @Transactional
    public User registerUser(UserRegisterRequest userJoinRequest) {
        User user = User.builder()
                .nickName(userJoinRequest.getNickname())
                .email(userJoinRequest.getEmail())
                .build();
        userRepository.save(user);
        return user;
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        //소셜로그인 정보의 경우 탈퇴 후에도 불필요한 정보. 재가입을 고려해 바로 삭제
        socialLoginUserRepository.deleteByUser(user);
        //유저 정보의 경우 게시글 유지를 위해 soft delete
        user.setDeleted(true);
        //다른 유저가 사용 가능하도록 닉네임을 null로 변경
        user.setNickname(null);
    }
}
