package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.constant.RecruitmentState;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "모잡글 id")
    private Long id;

    @ApiModelProperty(value = "모집 상태")
    private RecruitmentState state;

    @ApiModelProperty(value = "참여 인원")
    private Integer headCount;

    @ApiModelProperty(value = "모집 인원")
    private Integer headCountMax;

    @ApiModelProperty(value = "시작 시간")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "종료 시간")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "이벤트 id")
    private Long eventId;

    @ApiModelProperty(value = "게시글 id")
    private Long postId;

    @ApiModelProperty(value = "등록자 닉네임")
    private String username;

    @ApiModelProperty(value = "게시글 제목")
    private String title;

    @ApiModelProperty(value = "게시글 내용")
    private String content;

    @ApiModelProperty(value = "태그들", example = "[\"물놀이\",\"바베큐\"]")
    private List<String> tags;

    @ApiModelProperty(value = "대표이미지 경로")
    private String headImageUrl;

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

    public static RecruitmentDetailResponse of(Recruitment recruitment, List<Long> imageIds, List<String> imageUrls) {
        return RecruitmentDetailResponse.builder()
                .id(recruitment.getId())
                .state(recruitment.getState())
                .headCount(recruitment.getHeadCount())
                .headCountMax(recruitment.getHeadCountMax())
                .startDate(recruitment.getStartDate())
                .endDate(recruitment.getEndDate())
                .eventId(recruitment.getEvent().getId())
                .postId(recruitment.getPost().getId())
                .username(recruitment.getPost().getUser().getNickname())
                .title(recruitment.getPost().getTitle())
                .content(recruitment.getPost().getContent())
                .tags(Arrays.asList(recruitment.getPost().getTags().split(" ")))
                .headImageUrl(recruitment.getPost().getImage_url())
                .imageIds(imageIds)
                .imageUrls(imageUrls)
                .likeCount(recruitment.getPost().getLikeCount())
                .commentCount(recruitment.getPost().getCommentCount())
                .views(recruitment.getPost().getViews())
                .createdAt(recruitment.getPost().getCreatedAt())
                .lastModifiedAt(recruitment.getPost().getLastModifiedAt())
                .build();
    }
}
