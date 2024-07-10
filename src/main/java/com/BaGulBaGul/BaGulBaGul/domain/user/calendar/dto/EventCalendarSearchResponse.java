package com.BaGulBaGul.BaGulBaGul.domain.user.calendar.dto;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.user.EventCalendar;
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
public class EventCalendarSearchResponse {
    @ApiModelProperty(value = "이벤트 id")
    private Long eventId;
    @ApiModelProperty(value = "삭제 여부")
    private boolean deleted;
    @ApiModelProperty(value = "이벤트 종류")
    private EventType type;
    @ApiModelProperty(value = "게시글 제목")
    private String title;
    @ApiModelProperty(value = "게시글 내용")
    private String content;
    @ApiModelProperty(value = "게시글 썸네일 이미지 url")
    private String headImageUrl;
    @ApiModelProperty(value = "요약 주소")
    private String abstractLocation;
    @ApiModelProperty(value = "시작 시간")
    private LocalDateTime startTime;
    @ApiModelProperty(value = "종료 시간")
    private LocalDateTime endTime;

    public static EventCalendarSearchResponse of(EventCalendar eventCalendar) {
        Event event = eventCalendar.getEvent();
        Post post = event.getPost();
        return EventCalendarSearchResponse.builder()
                .eventId(event.getId())
                .deleted(event.getDeleted())
                .type(event.getType())
                .title(post.getTitle())
                .content(post.getContent())
                .headImageUrl(post.getImage_url())
                .abstractLocation(event.getAbstractLocation())
                .startTime(event.getStartDate())
                .endTime(event.getEndDate())
                .build();
    }
}
