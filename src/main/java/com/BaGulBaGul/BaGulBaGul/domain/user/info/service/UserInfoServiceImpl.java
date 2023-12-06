package com.BaGulBaGul.BaGulBaGul.domain.user.info.service;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.UserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.UserModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import com.BaGulBaGul.BaGulBaGul.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final UserRepository userRepository;

    private final UserImageService userImageService;

    @Override
    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        return UserInfoResponse.builder()
                .id(userId)
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileMessage(user.getProfileMessage())
                .imageURI(user.getImageURI())
                .build();
    }

    @Override
    @Transactional
    public void modifyUserInfo(UserModifyRequest userModifyRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        //null이 아니라면 해당 필드를 변경
        if(userModifyRequest.getEmail().isPresent()) {
            user.setEmail(userModifyRequest.getEmail().get());
        }
        if(userModifyRequest.getProfileMessage().isPresent()) {
            user.setProfileMessage(userModifyRequest.getProfileMessage().get());
        }
        if(userModifyRequest.getImageResourceId().isPresent()) {
            userImageService.setImage(user, userModifyRequest.getImageResourceId().get());
        }
    }
}
