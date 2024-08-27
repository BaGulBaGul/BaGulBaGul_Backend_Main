package com.BaGulBaGul.BaGulBaGul.domain.user.calendar.event.dto;

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
public class EventCalendarDeleteRequest {
    private Long eventId;
}
