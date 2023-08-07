package com.BaGulBaGul.BaGulBaGul.domain.post.dto;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.constant.PostType;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class PostDetailResponse {
    private Long id;
    private PostType type;
    private String userName;
    private String title;
    private String content;
    private Integer headCount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<String> tags;
    private List<String> categories;
    private String image_url;
    private int likeCount;
    private int commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public static PostDetailResponse of(Post post) {
        List<String> tags = post.getTags().equals("") ?
                new ArrayList<>() :
                Arrays.asList(post.getTags().split(" ")).stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
        return PostDetailResponse.builder()
                .id(post.getId())
                .type(post.getType())
                .userName(post.getUser().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .headCount(post.getHeadCount())
                .startDate(post.getStartDate())
                .endDate(post.getEndDate())
                .tags(tags)
                .categories(
                        post.getCategories()
                                .stream()
                                .map(postCategory -> postCategory.getCategory().getName())
                                .collect(Collectors.toList())
                )
                .image_url(post.getImage_url())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .createdAt(post.getCreatedAt())
                .lastModifiedAt(post.getLastModifiedAt())
                .build();
    }
}
