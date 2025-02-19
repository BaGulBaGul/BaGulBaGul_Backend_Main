package com.BaGulBaGul.BaGulBaGul.domain.recruitment.controller;

import com.BaGulBaGul.BaGulBaGul.domain.event.applicationevent.QueryEventDetailByUserApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.LikeCountResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent.QueryRecruitmentDetailByUserApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response.GetLikeRecruitmentResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response.RecruitmentDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response.RecruitmentSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.request.RecruitmentModifyApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.request.RecruitmentPageApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.request.RecruitmentRegisterApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.response.GetLikeRecruitmentApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.response.RecruitmentDetailApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.response.RecruitmentIdApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.response.RecruitmentPageApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.service.RecruitmentService;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.IsMyLikeResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.validation.ValidationUtil;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @GetMapping("/recruitment/{recruitmentId}")
    @Operation(summary = "모집글 id를 받아 자세한 정보를 조회",
            description = "참고 : api 호출 시 조회수 1 증가"
    )
    public ApiResponse<RecruitmentDetailApiResponse> getRecruitmentById(
            @PathVariable(name="recruitmentId") Long recruitmentId
    ) {
        //모집글 상세조회
        RecruitmentDetailResponse recruitmentDetailResponse = recruitmentService.getRecruitmentDetailById(recruitmentId);
        //모집글을 유저가 상세조회 했을 경우에 대한 이벤트 발행
        applicationEventPublisher.publishEvent(
                new QueryRecruitmentDetailByUserApplicationEvent(recruitmentDetailResponse)
        );
        //api 응답으로 변환
        RecruitmentDetailApiResponse response = RecruitmentDetailApiResponse.from(recruitmentDetailResponse);
        return ApiResponse.of(response);
    }

    @Override
    @GetMapping("/recruitment")
    @Operation(summary = "주어진 조건에 맞는 모집글을 페이징 조회",
            description = "페이징 관련 파라메터는 \n "
                    + "http://localhost:8080/api/event/recruitment?eventId=7&size=10&page=0&sort=likeCount,desc&sort=views,asc \n "
                    + "이런 식으로 넘기면 됨 \n "
                    + "sort가 여러 개면 앞에서부터 순서대로 정렬 적용\n"
                    + "파라메터로 명시하지 않은 조건은 무시되지만 페이징은 기본 페이징 조건 적용됨(page 0 size 20)\n"
                    + "정렬 가능 속성 : createdAt, views, likeCount, commentCount, startDate, endDate, headCount"
    )
    public ApiResponse<Page<RecruitmentPageApiResponse>> getRecruitmentPageByCondition(
            RecruitmentPageApiRequest recruitmentPageApiRequest,
            Pageable pageable
    ) {
        //모집글 조건 조회 요청 생성 후 검증
        RecruitmentConditionalRequest recruitmentConditionalRequest = recruitmentPageApiRequest.toRecruitmentConditionalRequest();
        ValidationUtil.validate(recruitmentConditionalRequest);
        //페이지 조회
        Page<RecruitmentSimpleResponse> recruitmentPageByCondition = recruitmentService.getRecruitmentPageByCondition(
                recruitmentPageApiRequest.toRecruitmentConditionalRequest(),
                pageable
        );
        //페이지 api 응답 dto로 변환
        Page<RecruitmentPageApiResponse> responses = recruitmentPageByCondition.map(RecruitmentPageApiResponse::from);
        return ApiResponse.of(responses);
    }

    @Override
    @PostMapping("/{eventId}/recruitment")
    @Operation(summary = "어떤 이벤트에 모집글을 등록하는 api",
            description = "로그인 필요\n"
                    + "생성한 모집글 id 반환"
    )
    public ApiResponse<RecruitmentIdApiResponse> registerRecruitment(
            @PathVariable(name = "eventId") Long eventId,
            @AuthenticationPrincipal Long userId,
            @RequestBody RecruitmentRegisterApiRequest recruitmentRegisterApiRequest
    ) {
        //모집글 생성 요청 변환 후 검증
        RecruitmentRegisterRequest recruitmentRegisterRequest = recruitmentRegisterApiRequest.toRecruitmentRegisterRequest();
        ValidationUtil.validate(recruitmentRegisterRequest);
        //모집글 생성
        Long id = recruitmentService.registerRecruitment(eventId, userId, recruitmentRegisterRequest);
        return ApiResponse.of(new RecruitmentIdApiResponse(id));
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
            @RequestBody RecruitmentModifyApiRequest recruitmentModifyApiRequest
    ) {
        //모집글 수정 요청으로 변환 후 검증
        RecruitmentModifyRequest recruitmentModifyRequest = recruitmentModifyApiRequest.toRecruitmentModifyRequest();
        ValidationUtil.validate(recruitmentModifyRequest);
        //모집글 수정
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
    public ApiResponse<LikeCountResponse> addLike(
            @PathVariable(name="recruitmentId") Long recruitmentId,
            @AuthenticationPrincipal Long userId
    ) {
        try {
            recruitmentService.addLike(recruitmentId, userId);
        } catch (DuplicateLikeException e) {
        }
        return ApiResponse.of(
                new LikeCountResponse(
                        recruitmentService.getLikeCount(recruitmentId)
                )
        );
    }

    @Override
    @DeleteMapping("/recruitment/{recruitmentId}/like")
    @Operation(summary = "모집글 좋아요 삭제 api",
            description = "로그인 필요\n"
                    + "삭제할 좋아요가 없다면 무시됨"
    )
    public ApiResponse<LikeCountResponse> deleteLike(
            @PathVariable(name="recruitmentId") Long recruitmentId,
            @AuthenticationPrincipal Long userId
    ) {
        try {
            recruitmentService.deleteLike(recruitmentId, userId);
        } catch (LikeNotExistException e) {
        }
        return ApiResponse.of(
                new LikeCountResponse(
                        recruitmentService.getLikeCount(recruitmentId)
                )
        );
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
    public ApiResponse<Page<GetLikeRecruitmentApiResponse>> getMyLike(
            @AuthenticationPrincipal Long userId,
            Pageable pageable
    ) {
        //좋아요 누른 페이지 검색
        Page<GetLikeRecruitmentResponse> myLikeRecruitments = recruitmentService.getMyLikeRecruitment(userId, pageable);
        //api 응답 dto로 변환
        Page<GetLikeRecruitmentApiResponse> responses = myLikeRecruitments.map(GetLikeRecruitmentApiResponse::from);
        return ApiResponse.of(responses);
    }
}
