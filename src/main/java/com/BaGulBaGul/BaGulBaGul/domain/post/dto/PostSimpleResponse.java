package com.BaGulBaGul.BaGulBaGul.domain.post.dto;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.constant.PostType;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class PostSimpleResponse {
    private Long id;
    private PostType type;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<String> tags;
    private List<String> categories;
    private String image_url;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public static PostSimpleResponse of(Post post) {
        return PostSimpleResponse.builder()
                .id(post.getId())
                .type(post.getType())
                .title(post.getTitle())
                .startDate(post.getStartDate())
                .endDate(post.getEndDate())
                .tags(Arrays.asList(post.getTags().split(" ")))
                .categories(
                        post.getCategories()
                                .stream()
                                .map(postCategory -> postCategory.getCategory().getName())
                                .collect(Collectors.toList())
                )
                .image_url(post.getImage_url())
                .createdAt(post.getCreatedAt())
                .lastModifiedAt(post.getLastModifiedAt())
                .build();
    }
}
