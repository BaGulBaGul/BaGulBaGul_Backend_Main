package com.BaGulBaGul.BaGulBaGul.domain.recruitment.controller;

import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.GetLikeRecruitmentResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.IsMyLikeResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentConditionalApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentRegisterResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.service.RecruitmentService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
@Api(tags = "모집글")
public class RecruitmentControllerImpl implements RecruitmentController {

    private final RecruitmentService recruitmentService;

    @Override
    @GetMapping("/recruitment/{recruitmentId}")
    public ApiResponse<RecruitmentDetailResponse> getRecruitmentById(
            @PathVariable(name="recruitmentId") Long recruitmentId
    ) {
        RecruitmentDetailResponse recruitmentDetailResponse = recruitmentService.getRecruitmentDetailById(recruitmentId);
        return ApiResponse.of(recruitmentDetailResponse);
    }

    @Override
    @GetMapping("/{eventId}/recruitment")
    public ApiResponse<Page<RecruitmentSimpleResponse>> getRecruitmentPageByCondition(
            @PathVariable(name = "eventId") Long eventId,
            RecruitmentConditionalApiRequest recruitmentConditionalApiRequest,
            Pageable pageable
    ) {
        return ApiResponse.of(
            recruitmentService.getRecruitmentPageByCondition(
                    recruitmentConditionalApiRequest.toRecruitmentConditionalRequest(eventId),
                    pageable
            )
        );
    }

    @Override
    @PostMapping("/{eventId}/recruitment")
    public ApiResponse<RecruitmentRegisterResponse> registerRecruitment(
            @PathVariable(name = "eventId") Long eventId,
            Long userId,
            @RequestBody RecruitmentRegisterRequest recruitmentRegisterRequest
    ) {
        Long id = recruitmentService.registerRecruitment(eventId, userId, recruitmentRegisterRequest);
        return ApiResponse.of(
                new RecruitmentRegisterResponse(id)
        );
    }

    @Override
    @PatchMapping("/recruitment/{recruitmentId}")
    public ApiResponse<Object> modifyRecruitment(
            @PathVariable(name="recruitmentId") Long recruitmentId,
            Long userId,
            @RequestBody RecruitmentModifyRequest recruitmentModifyRequest
    ) {
        recruitmentService.modifyRecruitment(recruitmentId, userId, recruitmentModifyRequest);
        return ApiResponse.of(null);
    }

    @Override
    @DeleteMapping("/recruitment/{recruitmentId}")
    public ApiResponse<Object> deleteRecruitment(
            @PathVariable(name="recruitmentId") Long recruitmentId,
            Long userId
    ) {
        recruitmentService.deleteRecruitment(recruitmentId, userId);
        return ApiResponse.of(null);
    }

    @Override
    @PostMapping("/recruitment/{recruitmentId}/like")
    public ApiResponse<Object> addLike(
            @PathVariable(name="recruitmentId") Long recruitmentId,
            Long userId
    ) {
        try {
            recruitmentService.addLike(recruitmentId, userId);
        } catch (DuplicateLikeException e) {
        }
        return ApiResponse.of(null);
    }

    @Override
    @DeleteMapping("/recruitment/{recruitmentId}/like")
    public ApiResponse<Object> deleteLike(
            @PathVariable(name="recruitmentId") Long recruitmentId,
            Long userId
    ) {
        try {
            recruitmentService.deleteLike(recruitmentId, userId);
        } catch (LikeNotExistException e) {
        }
        return ApiResponse.of(null);
    }

    @Override
    @GetMapping("/recruitment/{recruitmentId}/ismylike")
    public ApiResponse<IsMyLikeResponse> isMyLike(
            @PathVariable(name="recruitmentId") Long recruitmentId,
            Long userId
    ) {
        return ApiResponse.of(
                new IsMyLikeResponse(recruitmentService.isMyLike(recruitmentId, userId))
        );
    }

    @Override
    @GetMapping("/recruitment/mylike")
    public ApiResponse<Page<GetLikeRecruitmentResponse>> getMyLike(
            Long userId,
            Pageable pageable
    ) {
        return ApiResponse.of(
                recruitmentService.getMyLikeRecruitment(userId, pageable)
        );
    }
}
