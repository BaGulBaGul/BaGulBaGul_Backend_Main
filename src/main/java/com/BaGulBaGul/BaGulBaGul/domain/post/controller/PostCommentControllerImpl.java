package com.BaGulBaGul.BaGulBaGul.domain.post.controller;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetPostCommentChildPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetPostCommentPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentChildModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentChildRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentChildRegisterResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentRegisterResponse;
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
@RequestMapping("/api/post")
@RequiredArgsConstructor
@Api(tags = "게시글 댓글 + 대댓글")
public class PostCommentControllerImpl implements PostCommentController {

    private final PostCommentService postCommentService;

    @Override
    @GetMapping("/{postId}/comment")
    @Operation(summary = "어떤 게시글에 대한 모든 댓글 검색",
            description = "로그인을 했다면 자신이 좋아요를 눌렀는지 여부를 함께 반환\n"
                    + "로그인하지 않았다면 좋아요 여부는 전부 false\n"
                    + "페이징 지원"
    )
    public ApiResponse<Page<GetPostCommentPageResponse>> getPostCommentPage(
            @PathVariable(name = "postId") Long postId,
            @AuthenticationPrincipal Long requestUserId,
            Pageable pageable
    ) {
        return ApiResponse.of(postCommentService.getPostCommentPage(postId, requestUserId, pageable));
    }

    @Override
    @GetMapping("/comment/{postCommentId}/children")
    @Operation(summary = "어떤 댓글에 대한 모든 대댓글 검색",
            description = "로그인을 했다면 자신이 좋아요를 눌렀는지 여부를 함께 반환\n"
                    + "로그인하지 않았다면 좋아요 여부는 전부 false\n"
                    + "페이징 지원"
    )
    public ApiResponse<Page<GetPostCommentChildPageResponse>> getPostCommentChildPage(
            @PathVariable(name = "postCommentId") Long postCommentId,
            @AuthenticationPrincipal Long requestUserId,
            Pageable pageable
    ) {
        return ApiResponse.of(
                postCommentService.getPostCommentChildPage(
                        postCommentId, requestUserId, pageable
                )
        );
    }

    @Override
    @PostMapping("/{postId}/comment")
    @Operation(summary = "어떤 게시글에 대해 댓글 등록",
            description = "로그인 필요\n"
                    + "생성한 댓글 id 반환"
    )
    public ApiResponse<PostCommentRegisterResponse> registerPostComment(
            @PathVariable(name = "postId") Long postId,
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid PostCommentRegisterRequest postCommentRegisterRequest
    ) {
        Long postCommentId = postCommentService.registerPostComment(postId, userId, postCommentRegisterRequest);
        return ApiResponse.of(
                new PostCommentRegisterResponse(postCommentId)
        );
    }

    @Override
    @PatchMapping("comment/{postCommentId}")
    @Operation(summary = "댓글 수정",
            description = "로그인 필요\n"
                    + "PATCH방식. 변경하기 싫은 필드는 보내지 않거나 null로 보내면 됨"
    )
    public ApiResponse<Object> modifyPostComment(
            @PathVariable(name = "postCommentId") Long postCommentId,
            @AuthenticationPrincipal Long userId,
            @RequestBody PostCommentModifyRequest postCommentModifyRequest
    ) {
        postCommentService.modifyPostComment(postCommentId, userId, postCommentModifyRequest);
        return ApiResponse.of(null);
    }

    @Override
    @DeleteMapping("comment/{postCommentId}")
    @Operation(summary = "댓글 삭제",
            description = "로그인 필요"
    )
    public ApiResponse<Object> deletePostComment(
            @PathVariable(name = "postCommentId") Long postCommentId,
            @AuthenticationPrincipal Long userId
    ) {
        postCommentService.deletePostComment(postCommentId, userId);
        return ApiResponse.of(null);
    }

    @Override
    @PostMapping("/comment/{postCommentId}/children")
    @Operation(summary = "어떤 댓글에 대한 대댓글 등록",
            description = "로그인 필요\n"
                    + "생성한 대댓글 id 반환"
    )
    public ApiResponse<PostCommentChildRegisterResponse> registerPostCommentChild(
            @PathVariable(name = "postCommentId") Long postCommentId,
            @AuthenticationPrincipal Long userId,
            @RequestBody @Valid PostCommentChildRegisterRequest postCommentChildRegisterRequest
    ) {
        Long postCommentChildId = postCommentService.registerPostCommentChild(postCommentId, userId, postCommentChildRegisterRequest);
        return ApiResponse.of(
                new PostCommentChildRegisterResponse(postCommentChildId)
        );
    }

    @Override
    @PatchMapping("/comment/children/{postCommentChildId}")
    @Operation(summary = "대댓글 수정",
            description = "로그인 필요\n"
                    + "PATCH방식. 변경하기 싫은 필드는 보내지 않거나 null로 보내면 됨"
    )
    public ApiResponse<Object> modifyPostCommentChild(
            @PathVariable(name = "postCommentChildId") Long postCommentChildId,
            @AuthenticationPrincipal Long userId,
            @RequestBody PostCommentChildModifyRequest postCommentChildModifyRequest
    ) {
        postCommentService.modifyPostCommentChild(postCommentChildId, userId, postCommentChildModifyRequest);
        return ApiResponse.of(null);
    }

    @Override
    @DeleteMapping("/comment/children/{postCommentChildId}")
    @Operation(summary = "대댓글 삭제",
            description = "로그인 필요"
    )
    public ApiResponse<Object> deletePostCommentChild(
            @PathVariable(name = "postCommentChildId") Long postCommentChildId,
            @AuthenticationPrincipal Long userId
    ) {
        postCommentService.deletePostCommentChild(postCommentChildId, userId);
        return ApiResponse.of(null);
    }

    @Override
    @PostMapping("comment/{postCommentId}/like")
    @Operation(summary = "댓글 좋아요 등록",
            description = "로그인 필요\n"
                    + "유저당 한번만 좋아요 등록 가능\n"
                    + "이미 좋아요를 눌렀다면 무시됨"
    )
    public ApiResponse<Object> addLikeToComment(
            @PathVariable(name = "postCommentId") Long postCommentId,
            @AuthenticationPrincipal Long userId
    ) {
        try {
            postCommentService.addLikeToComment(postCommentId, userId);
        }
        catch (DuplicateLikeException duplicateLikeException) {
        }
        return ApiResponse.of(null);
    }


    @Override
    @DeleteMapping("comment/{postCommentId}/like")
    @Operation(summary = "댓글 좋아요 삭제",
            description = "로그인 필요\n"
                    + "삭제할 좋아요가 없다면 무시됨"
    )
    public ApiResponse<Object> deleteLikeToComment(
            @PathVariable(name = "postCommentId") Long postCommentId,
            @AuthenticationPrincipal Long userId
    ) {
        try {
            postCommentService.deleteLikeToComment(postCommentId, userId);
        }
        catch (LikeNotExistException likeNotExistException) {
        }
        return ApiResponse.of(null);
    }

    @Override
    @PostMapping("comment/children/{postCommentChildId}/like")
    @Operation(summary = "대댓글 좋아요 등록",
            description = "로그인 필요\n"
                    + "유저당 한번만 좋아요 등록 가능\n"
                    + "이미 좋아요를 눌렀다면 무시됨"
    )
    public ApiResponse<Object> addLikeToCommentChild(
            @PathVariable(name = "postCommentChildId") Long postCommentChildId,
            @AuthenticationPrincipal Long userId
    ) {
        try {
            postCommentService.addLikeToCommentChild(postCommentChildId, userId);
        }
        catch (DuplicateLikeException duplicateLikeException) {
        }
        return ApiResponse.of(null);
    }

    @Override
    @DeleteMapping("comment/children/{postCommentChildId}/like")
    @Operation(summary = "대댓글 좋아요 삭제",
            description = "로그인 필요\n"
                    + "삭제할 좋아요가 없다면 무시됨"
    )
    public ApiResponse<Object> deleteLikeToCommentChild(
            @PathVariable(name = "postCommentChildId") Long postCommentChildId,
            @AuthenticationPrincipal Long userId
    ) {
        try {
            postCommentService.deleteLikeToCommentChild(postCommentChildId, userId);
        }
        catch (LikeNotExistException likeNotExistException) {
        }
        return ApiResponse.of(null);
    }
}