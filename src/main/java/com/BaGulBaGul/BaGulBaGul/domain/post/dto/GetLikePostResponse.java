package com.BaGulBaGul.BaGulBaGul.domain.post.dto;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class GetLikePostResponse {
    private Long id;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public static GetLikePostResponse of(Post post) {
        return GetLikePostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .startDate(post.getStartDate())
                .endDate(post.getEndDate())
                .build();
    }
}
