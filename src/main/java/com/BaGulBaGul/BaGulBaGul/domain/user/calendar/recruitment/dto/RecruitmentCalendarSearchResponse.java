package com.BaGulBaGul.BaGulBaGul.domain.user.calendar.recruitment.dto;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.constant.RecruitmentState;
import com.BaGulBaGul.BaGulBaGul.domain.user.RecruitmentCalendar;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecruitmentCalendarSearchResponse {
    @ApiModelProperty(value = "모집글 id")
    private Long recruitmentId;
    @ApiModelProperty(value = "삭제 여부")
    private boolean deleted;
    @ApiModelProperty(value = "모집 상태")
    private RecruitmentState state;
    @ApiModelProperty(value = "게시글 제목")
    private String title;
    @ApiModelProperty(value = "게시글 내용")
    private String content;
    @ApiModelProperty(value = "게시글 썸네일 이미지 url")
    private String headImageUrl;
    @ApiModelProperty(value = "시작 시간")
    private LocalDateTime startTime;
    @ApiModelProperty(value = "종료 시간")
    private LocalDateTime endTime;

    public static RecruitmentCalendarSearchResponse of(RecruitmentCalendar recruitmentCalendar) {
        Recruitment recruitment = recruitmentCalendar.getRecruitment();
        Post post = recruitment.getPost();
        return RecruitmentCalendarSearchResponse.builder()
                .recruitmentId(recruitment.getId())
                .deleted(recruitment.getDeleted())
                .state(recruitment.getState())
                .title(post.getTitle())
                .content(post.getContent())
                .headImageUrl(post.getImage_url())
                .startTime(recruitment.getStartDate())
                .endTime(recruitment.getEndDate())
                .build();
    }
}
