package com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response;

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
public class PostDetailInfo {
    private Long postId;

    private PostWriterInfo writer;

    private String title;

    private String headImageUrl;

    private String content;

    private List<String> tags;

    private List<Long> imageIds;

    private List<String> imageUrls;

    private int likeCount;

    private int commentCount;

    private int views;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;
}
