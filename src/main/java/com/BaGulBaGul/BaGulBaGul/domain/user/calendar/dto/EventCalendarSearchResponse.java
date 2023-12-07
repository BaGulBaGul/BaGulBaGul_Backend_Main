package com.BaGulBaGul.BaGulBaGul.domain.user.calendar.dto;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.user.EventCalendar;
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
    private EventType type;
    private String title;
    private String content;
    private String headImageUrl;
    private String abstractLocation;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public static EventCalendarSearchResponse of(EventCalendar eventCalendar) {
        Event event = eventCalendar.getEvent();
        Post post = event.getPost();
        return EventCalendarSearchResponse.builder()
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
