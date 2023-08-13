package com.BaGulBaGul.BaGulBaGul.domain.recruitment.service;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentResponseDto;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.RecruitmentCommentChildRepository;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitmentCommentChildService {
    private final RecruitmentCommentChildRepository recruitmentCommentChildRepository;

    public ApiResponse<List<RecruitmentResponseDto.RCommentChildWithPaging>> getRecruitmentCommentChild(Long id, Integer offset){

        List<RecruitmentResponseDto.RCommentChildWithPaging> request = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(offset, 10);

        // 작성자인지 판단하는 코드 추가 예정 (토큰으로 판단?)

        recruitmentCommentChildRepository.findCommentChildByRecruitmentCommentIdOrderByCreatedAt(id, pageRequest)
                .forEach(r -> request.add(RecruitmentResponseDto.RCommentChildWithPaging
                        .builder()
                        .userImageURI(r.getUser().getImageURI())
                        .content(r.getContent())
                        .isWriter(false)
                        .createdAt(r.getCreatedAt())
                        .lastModifiedAt(r.getLastModifiedAt())
                        .build()));

        return ApiResponse.of(request);
    }
}
