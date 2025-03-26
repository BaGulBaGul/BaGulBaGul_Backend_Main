package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostLikeRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.event.repository.EventCalendarRepository;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.recruitment.repository.RecruitmentCalendarRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.DuplicateUsernameException;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.UserNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.MyUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.OtherUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.UserModifyRequest;
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
    public MyUserInfoResponse getMyUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        long writingCount = postRepository.countByUserId(userId);
        long postLikeCount = postLikeRepository.countByUserId(userId);
        long calendarCount = eventCalendarRepository.countByUserId(userId) + recruitmentCalendarRepository.countByUserId(userId);
        return MyUserInfoResponse.builder()
                .id(userId)
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileMessage(user.getProfileMessage())
                .imageURI(user.getImageURI())
                .writingCount(writingCount)
                .postLikeCount(postLikeCount)
                .calendarCount(calendarCount)
                .build();
    }

    @Override
    public OtherUserInfoResponse getOtherUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        long writingCount = postRepository.countByUserId(userId);
        return OtherUserInfoResponse.builder()
                .id(userId)
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileMessage(user.getProfileMessage())
                .imageURI(user.getImageURI())
                .writingCount(writingCount)
                .build();
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
}
