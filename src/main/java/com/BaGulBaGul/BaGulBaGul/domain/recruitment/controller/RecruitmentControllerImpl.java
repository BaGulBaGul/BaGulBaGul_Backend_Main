package com.BaGulBaGul.BaGulBaGul.domain.recruitment.controller;

import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.GetLikeRecruitmentResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentConditionalApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentRegisterResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.service.RecruitmentService;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.IsMyLikeResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    @Operation(summary = "모집글 id를 받아 자세한 정보를 조회",
            description = "참고 : api 호출 시 조회수 1 증가"
    )
    public ApiResponse<RecruitmentDetailResponse> getRecruitmentById(
            @PathVariable(name="recruitmentId") Long recruitmentId
    ) {
        RecruitmentDetailResponse recruitmentDetailResponse = recruitmentService.getRecruitmentDetailById(recruitmentId);
        return ApiResponse.of(recruitmentDetailResponse);
    }

    @Override
    @GetMapping("/{eventId}/recruitment")
    @Operation(summary = "어떤 이벤트에 속하면서 주어진 조건에 맞는 모집글을 페이징 조회",
            description = "페이징 관련 파라메터는 \n "
                    + "http://localhost:8080/api/event/7/recruitment?size=10&page=0&sort=likeCount,desc&sort=views,asc \n "
                    + "이런 식으로 넘기면 됨 \n "
                    + "sort가 여러 개면 앞에서부터 순서대로 정렬 적용\n"
                    + "파라메터로 명시하지 않은 조건은 무시되지만 페이징은 기본 페이징 조건 적용됨(page 0 size 20)\n"
                    + "정렬 가능 속성 : createdAt, views, likeCount, commentCount, startDate, endDate, headCount"
    )
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
    @Operation(summary = "어떤 이벤트에 모집글을 등록하는 api",
            description = "로그인 필요\n"
                    + "생성한 모집글 id 반환"
    )
    public ApiResponse<RecruitmentRegisterResponse> registerRecruitment(
            @PathVariable(name = "eventId") Long eventId,
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid RecruitmentRegisterRequest recruitmentRegisterRequest
    ) {
        Long id = recruitmentService.registerRecruitment(eventId, userId, recruitmentRegisterRequest);
        return ApiResponse.of(
                new RecruitmentRegisterResponse(id)
        );
    }

    @Override
    @PatchMapping("/recruitment/{recruitmentId}")
    @Operation(summary = "모집글 수정 api",
            description = "로그인 필요\n"
                    + "PATCH방식. 변경하기 싫은 필드는 보내지 않거나 null로 보내면 됨"
    )
    public ApiResponse<Object> modifyRecruitment(
            @PathVariable(name="recruitmentId") Long recruitmentId,
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid RecruitmentModifyRequest recruitmentModifyRequest
    ) {
        recruitmentService.modifyRecruitment(recruitmentId, userId, recruitmentModifyRequest);
        return ApiResponse.of(null);
    }

    @Override
    @DeleteMapping("/recruitment/{recruitmentId}")
    @Operation(summary = "모집글 삭제 api",
            description = "로그인 필요"
    )
    public ApiResponse<Object> deleteRecruitment(
            @PathVariable(name="recruitmentId") Long recruitmentId,
            @AuthenticationPrincipal Long userId
    ) {
        recruitmentService.deleteRecruitment(recruitmentId, userId);
        return ApiResponse.of(null);
    }

    @Override
    @PostMapping("/recruitment/{recruitmentId}/like")
    @Operation(summary = "모집글 좋아요 등록 api",
            description = "로그인 필요\n"
                    + "유저당 한번만 좋아요 등록 가능\n"
                    + "이미 좋아요를 눌렀다면 무시됨"
    )
    public ApiResponse<Object> addLike(
            @PathVariable(name="recruitmentId") Long recruitmentId,
            @AuthenticationPrincipal Long userId
    ) {
        try {
            recruitmentService.addLike(recruitmentId, userId);
        } catch (DuplicateLikeException e) {
        }
        return ApiResponse.of(null);
    }

    @Override
    @DeleteMapping("/recruitment/{recruitmentId}/like")
    @Operation(summary = "모집글 좋아요 삭제 api",
            description = "로그인 필요\n"
                    + "삭제할 좋아요가 없다면 무시됨"
    )
    public ApiResponse<Object> deleteLike(
            @PathVariable(name="recruitmentId") Long recruitmentId,
            @AuthenticationPrincipal Long userId
    ) {
        try {
            recruitmentService.deleteLike(recruitmentId, userId);
        } catch (LikeNotExistException e) {
        }
        return ApiResponse.of(null);
    }

    @Override
    @GetMapping("/recruitment/{recruitmentId}/ismylike")
    @Operation(summary = "모집글 좋아요 확인 api",
            description = "로그인 필요\n"
                    + "자신이 좋아요를 눌렀는지 boolean으로 확인"
    )
    public ApiResponse<IsMyLikeResponse> isMyLike(
            @PathVariable(name="recruitmentId") Long recruitmentId,
            @AuthenticationPrincipal Long userId
    ) {
        return ApiResponse.of(
                new IsMyLikeResponse(recruitmentService.isMyLike(recruitmentId, userId))
        );
    }

    @Override
    @GetMapping("/recruitment/mylike")
    @Operation(summary = "자신이 좋아요를 누른 모든 모집글 검색",
            description = "로그인 필요\n"
                    + "페이징 지원"
    )
    public ApiResponse<Page<GetLikeRecruitmentResponse>> getMyLike(
            @AuthenticationPrincipal Long userId,
            Pageable pageable
    ) {
        return ApiResponse.of(
                recruitmentService.getMyLikeRecruitment(userId, pageable)
        );
    }
}
