package com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostDetailInfo;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostWriterInfo;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostDetailApiInfo {
    @ApiModelProperty(value = "게시글 id")
    private Long postId;

    @ApiModelProperty(value = "작성자 정보")
    private PostWriterApiInfo writer;

    @ApiModelProperty(value = "게시글 제목")
    private String title;

    @ApiModelProperty(value = "대표이미지 경로")
    private String headImageUrl;

    @ApiModelProperty(value = "게시글 내용")
    private String content;

    @ApiModelProperty(value = "태그들", example = "[\"물놀이\",\"바베큐\"]")
    private List<String> tags;

    @ApiModelProperty(value = "이미지들의 resource id")
    private List<Long> imageIds;

    @ApiModelProperty(value = "이미지들의 url")
    private List<String> imageUrls;

    @ApiModelProperty(value = "종아요 수")
    private int likeCount;

    @ApiModelProperty(value = "댓글 수")
    private int commentCount;

    @ApiModelProperty(value = "조회 수")
    private int views;

    @ApiModelProperty(value = "생성일")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "마지막 수정일")
    private LocalDateTime lastModifiedAt;

    public static PostDetailApiInfo from(PostDetailInfo postDetailInfo) {
        return PostDetailApiInfo.builder()
                .postId(postDetailInfo.getPostId())
                .writer(PostWriterApiInfo.from(postDetailInfo.getWriter()))
                .title(postDetailInfo.getTitle())
                .headImageUrl(postDetailInfo.getHeadImageUrl())
                .content(postDetailInfo.getContent())
                .tags(postDetailInfo.getTags())
                .imageIds(postDetailInfo.getImageIds())
                .likeCount(postDetailInfo.getLikeCount())
                .commentCount(postDetailInfo.getCommentCount())
                .views(postDetailInfo.getViews())
                .createdAt(postDetailInfo.getCreatedAt())
                .lastModifiedAt(postDetailInfo.getLastModifiedAt())
                .build();
    }
}
