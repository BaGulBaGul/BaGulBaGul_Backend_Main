package com.BaGulBaGul.BaGulBaGul.domain.user.info.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.SocialLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.auth.service.JwtProvider;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.SocialLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.UserRegisterRequest;
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
        OAuth2JoinTokenSubject oAuth2JoinTokenSubject = Optional.of(jwtProvider.getOAuth2JoinTokenSubject(joinToken))
                .orElseThrow(() -> new GeneralException(ErrorCode.UJ_WRONG_JOINTOKEN));
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
}
