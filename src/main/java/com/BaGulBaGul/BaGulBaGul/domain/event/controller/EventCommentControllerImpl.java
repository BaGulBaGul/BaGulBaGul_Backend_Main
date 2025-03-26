package com.BaGulBaGul.BaGulBaGul.domain.event.controller;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.response.EventIdApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.EventCommentService;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.GetPostCommentChildPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.GetPostCommentPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.IsMyLikeResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.LikeCountResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentChildModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentChildRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.PostCommentChildRegisterResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.PostCommentDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.PostCommentRegisterResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostCommentService;
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
@Api(tags = "게시글 댓글 + 대댓글")
public class EventCommentControllerImpl implements EventCommentController {

    private final PostCommentService postCommentService;
    private final EventCommentService eventCommentService;

    @Override
    @GetMapping("/comment/{commentId}")
    public ApiResponse<PostCommentDetailResponse> getCommentDetail(
            @PathVariable(name = "commentId") Long commentId
    ) {
        return ApiResponse.of(
                eventCommentService.getPostCommentDetail(commentId)
        );
    }

    @Override
    @GetMapping("/{eventId}/comment")
    @Operation(summary = "어떤 이벤트에 대한 모든 댓글 검색",
            description = "로그인을 했다면 자신이 좋아요를 눌렀는지 여부를 함께 반환\n"
                    + "로그인하지 않았다면 좋아요 여부는 전부 false\n"
                    + "페이징 지원"
    )
    public ApiResponse<Page<GetPostCommentPageResponse>> getEventCommentPage(
            @PathVariable(name = "eventId") Long eventId,
            @AuthenticationPrincipal Long requestUserId,
            Pageable pageable
    ) {
        return ApiResponse.of(eventCommentService.getEventCommentPage(eventId, requestUserId, pageable));
    }

    @Override
    @GetMapping("/comment/{commentId}/children")
    @Operation(summary = "어떤 댓글에 대한 모든 대댓글 검색",
            description = "로그인을 했다면 자신이 좋아요를 눌렀는지 여부를 함께 반환\n"
                    + "로그인하지 않았다면 좋아요 여부는 전부 false\n"
                    + "페이징 지원"
    )
    public ApiResponse<Page<GetPostCommentChildPageResponse>> getEventCommentChildPage(
            @PathVariable(name = "commentId") Long commentId,
            @AuthenticationPrincipal Long requestUserId,
            Pageable pageable
    ) {
        return ApiResponse.of(
                eventCommentService.getEventCommentChildPage(
                        commentId, requestUserId, pageable
                )
        );
    }

    @Override
    @PostMapping("/{eventId}/comment")
    @Operation(summary = "어떤 게시글에 대해 댓글 등록",
            description = "로그인 필요\n"
                    + "생성한 댓글 id 반환"
    )
    public ApiResponse<PostCommentRegisterResponse> registerComment(
            @PathVariable(name = "eventId") Long eventId,
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid PostCommentRegisterRequest postCommentRegisterRequest
    ) {
        Long commentId = eventCommentService.registerComment(eventId, userId, postCommentRegisterRequest);
        return ApiResponse.of(
                new PostCommentRegisterResponse(commentId)
        );
    }

    @Override
    @PatchMapping("/comment/{commentId}")
    @Operation(summary = "댓글 수정",
            description = "로그인 필요\n"
                    + "PATCH방식. 변경하기 싫은 필드는 보내지 않거나 null로 보내면 됨"
    )
    public ApiResponse<Object> modifyComment(
            @PathVariable(name = "commentId") Long commentId,
            @AuthenticationPrincipal Long userId,
            @RequestBody PostCommentModifyRequest postCommentModifyRequest
    ) {
        eventCommentService.modifyComment(commentId, userId, postCommentModifyRequest);
        return ApiResponse.of(null);
    }

    @Override
    @DeleteMapping("/comment/{commentId}")
    @Operation(summary = "댓글 삭제",
            description = "로그인 필요"
    )
    public ApiResponse<Object> deleteComment(
            @PathVariable(name = "commentId") Long commentId,
            @AuthenticationPrincipal Long userId
    ) {
        eventCommentService.deleteComment(commentId, userId);
        return ApiResponse.of(null);
    }

    @Override
    @PostMapping("/comment/{commentId}/children")
    @Operation(summary = "어떤 댓글에 대한 대댓글 등록",
            description = "로그인 필요\n"
                    + "생성한 대댓글 id 반환"
    )
    public ApiResponse<PostCommentChildRegisterResponse> registerCommentChild(
            @PathVariable(name = "commentId") Long commentId,
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid PostCommentChildRegisterRequest postCommentChildRegisterRequest
    ) {
        Long commentChildId = eventCommentService.registerCommentChild(commentId, userId, postCommentChildRegisterRequest);
        return ApiResponse.of(
                new PostCommentChildRegisterResponse(commentChildId)
        );
    }

    @Override
    @PatchMapping("/comment/children/{commentChildId}")
    @Operation(summary = "대댓글 수정",
            description = "로그인 필요\n"
                    + "PATCH방식. 변경하기 싫은 필드는 보내지 않거나 null로 보내면 됨"
    )
    public ApiResponse<Object> modifyCommentChild(
            @PathVariable(name = "commentChildId") Long commentChildId,
            @AuthenticationPrincipal Long userId,
            @RequestBody PostCommentChildModifyRequest postCommentChildModifyRequest
    ) {
        eventCommentService.modifyCommentChild(commentChildId, userId, postCommentChildModifyRequest);
        return ApiResponse.of(null);
    }

    @Override
    @DeleteMapping("/comment/children/{commentChildId}")
    @Operation(summary = "대댓글 삭제",
            description = "로그인 필요"
    )
    public ApiResponse<Object> deleteCommentChild(
            @PathVariable(name = "commentChildId") Long commentChildId,
            @AuthenticationPrincipal Long userId
    ) {
        eventCommentService.deleteCommentChild(commentChildId, userId);
        return ApiResponse.of(null);
    }

    @Override
    @PostMapping("/comment/{commentId}/like")
    @Operation(summary = "댓글 좋아요 등록",
            description = "로그인 필요\n"
                    + "유저당 한번만 좋아요 등록 가능\n"
                    + "이미 좋아요를 눌렀다면 무시됨"
    )
    public ApiResponse<LikeCountResponse> addLikeToComment(
            @PathVariable(name = "commentId") Long commentId,
            @AuthenticationPrincipal Long userId
    ) {
        try {
            eventCommentService.addLikeToComment(commentId, userId);
        } catch (DuplicateLikeException e) {
        }
        return ApiResponse.of(
                new LikeCountResponse(
                        postCommentService.getLikeCountFromComment(commentId)
                )
        );
    }


    @Override
    @DeleteMapping("/comment/{commentId}/like")
    @Operation(summary = "댓글 좋아요 삭제",
            description = "로그인 필요\n"
                    + "삭제할 좋아요가 없다면 무시됨"
    )
    public ApiResponse<LikeCountResponse> deleteLikeToComment(
            @PathVariable(name = "commentId") Long commentId,
            @AuthenticationPrincipal Long userId
    ) {
        try {
            eventCommentService.deleteLikeToComment(commentId, userId);
        } catch (LikeNotExistException e) {
        }
        return ApiResponse.of(
                new LikeCountResponse(
                        postCommentService.getLikeCountFromComment(commentId)
                )
        );
    }

    @Override
    @GetMapping("/comment/{commentId}/ismylike")
    @Operation(summary = "댓글에 유저가 좋아요를 눌렀는지 확인",
            description = "로그인 필요"
    )
    public ApiResponse<IsMyLikeResponse> isMyLikeComment(
            @PathVariable(name = "commentId") Long commentId,
            @AuthenticationPrincipal Long userId
    ) {
        return ApiResponse.of(
                new IsMyLikeResponse(
                        eventCommentService.existsCommentLike(commentId, userId)
                )
        );
    }

    @Override
    @PostMapping("/comment/children/{commentChildId}/like")
    @Operation(summary = "대댓글 좋아요 등록",
            description = "로그인 필요\n"
                    + "유저당 한번만 좋아요 등록 가능\n"
                    + "이미 좋아요를 눌렀다면 무시됨"
    )
    public ApiResponse<LikeCountResponse> addLikeToCommentChild(
            @PathVariable(name = "commentChildId") Long commentChildId,
            @AuthenticationPrincipal Long userId
    ) {
        try {
            eventCommentService.addLikeToCommentChild(commentChildId, userId);
        } catch (DuplicateLikeException e) {
        }
        return ApiResponse.of(
                new LikeCountResponse(
                        postCommentService.getLikeCountFromCommentChild(commentChildId)
                )
        );
    }

    @Override
    @DeleteMapping("/comment/children/{commentChildId}/like")
    @Operation(summary = "대댓글 좋아요 삭제",
            description = "로그인 필요\n"
                    + "삭제할 좋아요가 없다면 무시됨"
    )
    public ApiResponse<LikeCountResponse> deleteLikeToCommentChild(
            @PathVariable(name = "commentChildId") Long commentChildId,
            @AuthenticationPrincipal Long userId
    ) {
        try {
            eventCommentService.deleteLikeToCommentChild(commentChildId, userId);
        } catch (LikeNotExistException e) {
        }
        return ApiResponse.of(
                new LikeCountResponse(
                        postCommentService.getLikeCountFromCommentChild(commentChildId)
                )
        );
    }

    @Override
    @GetMapping("/comment/children/{commentChildId}/ismylike")
    @Operation(summary = "대댓글에 유저가 좋아요를 눌렀는지 확인",
            description = "로그인 필요"
    )
    public ApiResponse<IsMyLikeResponse> isMyLikeCommentChild(
            @PathVariable(name = "commentChildId") Long commentChildId,
            @AuthenticationPrincipal Long userId
    ) {
        return ApiResponse.of(
                new IsMyLikeResponse(
                        eventCommentService.existsCommentChildLike(commentChildId, userId)
                )
        );
    }

    @Override
    @GetMapping("/comment/{commentId}/eventId")
    @Operation(summary = "어떤 댓글이 속한 이벤트의 id를 조회",
            description = ""
    )
    public ApiResponse<EventIdApiResponse> getEventIdFromCommentId(
            @PathVariable(name = "commentId") Long commentId
    ) {
        Long eventId = eventCommentService.getEventIdFromCommentId(commentId);
        return ApiResponse.of(
                EventIdApiResponse.builder()
                        .eventId(eventId)
                        .build()
        );
    }
}