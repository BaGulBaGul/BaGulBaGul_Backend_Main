package com.BaGulBaGul.BaGulBaGul.domain.recruitment.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.contant.RecruitmentType;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentRequestDto;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentResponseDto;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.RecruitmentCommentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.RecruitmentLikeRepository;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.RecruitmentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitmentService {

    private final RecruitmentRepository recruitmentRepository;
    private final RecruitmentCommentRepository recruitmentCommentRepository;
    private final RecruitmentLikeRepository recruitmentLikeRepository;
    private final UserRepository userRepository;

    // 모집글 저장
    @Transactional
    public ApiResponse<Recruitment> save(RecruitmentRequestDto.RInfo requestDto){

        // 예외 처리 예정
        User user = userRepository.findById(requestDto.getUserId()).orElse(null);

        Recruitment recruitment = Recruitment.builder()
                .type(requestDto.getType())
                .user(user)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .headCount(requestDto.getHeadCount())
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .tags(requestDto.getTags())
                .imageURL(requestDto.getImageURL())
                .build();

        recruitmentRepository.save(recruitment);
        return ApiResponse.of(recruitment);

    }

    // 모집글 단일 조회
    public ApiResponse<RecruitmentResponseDto.RInfo> getRecruitment(Long id){

        // 예외 처리 예정
        Recruitment recruitment = recruitmentRepository.findById(id).orElse(null);

        return ApiResponse.of(RecruitmentResponseDto.RInfo.builder()
                .title(recruitment.getTitle())
                .content(recruitment.getContent())
                .tags(recruitment.getTags())
                .imageURL(recruitment.getImageURL())
                .startDate(recruitment.getStartDate())
                .endDate(recruitment.getEndDate())
                .commentCount(recruitmentCommentRepository.countByRecruitmentId(recruitment.getId()))
                .likeCount(recruitmentLikeRepository.countByRecruitmentId(recruitment.getId()))
                // 추후 개발
                .category("")
                // *****
                .createdAt(recruitment.getCreatedAt())
                .lastModifiedAt(recruitment.getLastModifiedAt())
                .build());
    }

    // 모집글 전체 조회
    public ApiResponse<List<RecruitmentResponseDto.RInfoWithPaging>> getRecruitments(Long id, Integer offset){
        List<RecruitmentResponseDto.RInfoWithPaging> request = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(offset, 20);

        recruitmentRepository.findRecruitmentByPostId(id, pageRequest)
                .forEach(r -> request.add(RecruitmentResponseDto.RInfoWithPaging
                        .builder()
                        .recruitmentId(r.getId())
                        .userImageURL(r.getUser().getImageURL())
                        .title(r.getTitle())
                        .content(r.getContent())
                        .startDate(r.getStartDate())
                        .endDate(r.getEndDate())
                        .tags(r.getTags())
                        .createdAt(r.getCreatedAt())
                        .lastModifiedAt(r.getLastModifiedAt())
                        .build()));
        return ApiResponse.of(request);
    }
}
