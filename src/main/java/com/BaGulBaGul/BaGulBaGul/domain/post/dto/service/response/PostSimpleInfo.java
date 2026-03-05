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
public class PostSimpleInfo {

    private Long postId;

    private PostWriterInfo writer;

    private String title;

    private String headImageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    private List<String> tags;

}
