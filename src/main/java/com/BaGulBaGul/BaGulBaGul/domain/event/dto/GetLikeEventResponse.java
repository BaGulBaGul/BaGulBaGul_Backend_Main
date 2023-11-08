package com.BaGulBaGul.BaGulBaGul.domain.event.dto;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class GetLikeEventResponse {
    private Long id;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public static GetLikeEventResponse of(Event event) {
        return GetLikeEventResponse.builder()
                .id(event.getId())
                .title(event.getPost().getTitle())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .build();
    }
}
