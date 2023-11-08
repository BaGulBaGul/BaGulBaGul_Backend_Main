package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.constant.RecruitmentState;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class RecruitmentDetailResponse {
    private Long id;
    private RecruitmentState state;
    private Integer headCount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long eventId;
    private Long postId;
    private String username;
    private String title;
    private String content;
    private List<String> tags;
    private String image_url;
    private int likeCount;
    private int commentCount;
    private int views;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public static RecruitmentDetailResponse of(Recruitment recruitment) {
        return RecruitmentDetailResponse.builder()
                .id(recruitment.getId())
                .state(recruitment.getState())
                .headCount(recruitment.getHeadCount())
                .startDate(recruitment.getStartDate())
                .endDate(recruitment.getEndDate())
                .eventId(recruitment.getEvent().getId())
                .postId(recruitment.getPost().getId())
                .username(recruitment.getPost().getUser().getNickname())
                .title(recruitment.getPost().getTitle())
                .content(recruitment.getPost().getContent())
                .tags(Arrays.asList(recruitment.getPost().getTags().split(" ")))
                .image_url(recruitment.getPost().getImage_url())
                .likeCount(recruitment.getPost().getLikeCount())
                .commentCount(recruitment.getPost().getCommentCount())
                .views(recruitment.getPost().getViews())
                .createdAt(recruitment.getPost().getCreatedAt())
                .lastModifiedAt(recruitment.getPost().getLastModifiedAt())
                .build();
    }
}
