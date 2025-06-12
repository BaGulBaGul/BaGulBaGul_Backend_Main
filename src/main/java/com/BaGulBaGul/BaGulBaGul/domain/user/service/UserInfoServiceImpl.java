package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostLikeRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.event.repository.EventCalendarRepository;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.recruitment.repository.RecruitmentCalendarRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.UserModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.MyUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.OtherUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.UserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.DuplicateUsernameException;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.UserNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final UserRepository userRepository;

    private final UserImageService userImageService;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final EventCalendarRepository eventCalendarRepository;
    private final RecruitmentCalendarRepository recruitmentCalendarRepository;


    @Override
    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        return UserInfoResponse.builder()
                .id(userId)
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileMessage(user.getProfileMessage())
                .imageURI(user.getImageURI())
                .build();
    }

    @Override
    public MyUserInfoResponse getMyUserInfo(Long userId) {
        UserInfoResponse userInfo = getUserInfo(userId);
        return MyUserInfoResponse.from(
                userInfo,
                getWritingCount(userId),
                getPostLikeCount(userId),
                getCalendarCount(userId)
        );
    }

    @Override
    public OtherUserInfoResponse getOtherUserInfo(Long userId) {
        UserInfoResponse userInfo = getUserInfo(userId);
        return OtherUserInfoResponse.from(
                userInfo,
                getWritingCount(userId)
        );
    }

    @Override
    @Transactional
    public void modifyUserInfo(UserModifyRequest userModifyRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
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
        //닉네임 변경
        if(userModifyRequest.getUsername().isPresent()) {
            String username = userModifyRequest.getUsername().get();
            //중복 닉네임 검사
            if(userRepository.existsByNicknameIgnoreCase(username)) {
                throw new DuplicateUsernameException();
            }
            //유저명 설정
            user.setNickname(username);
            //변경 시도(유니크 제약조건 검사) 나중에 aop로 리페터링 고려
            try {
                userRepository.flush();
            } catch (DataIntegrityViolationException e) {
                //유저명 유니크 제약조건 위반
                if(DuplicateUsernameException.check(e)) {
                    throw new DuplicateUsernameException();
                }
            }
        }
    }

    private long getWritingCount(long userId) {
        return postRepository.countByUserId(userId);
    }

    private long getCalendarCount(Long userId) {
        return eventCalendarRepository.countByUserId(userId) + recruitmentCalendarRepository.countByUserId(userId);
    }

    private long getPostLikeCount(Long userId) {
        return postLikeRepository.countByUserId(userId);
    }
}
