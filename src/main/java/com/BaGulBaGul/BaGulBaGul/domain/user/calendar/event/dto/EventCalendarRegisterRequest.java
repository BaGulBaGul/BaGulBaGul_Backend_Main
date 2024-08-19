package com.BaGulBaGul.BaGulBaGul.domain.user.calendar.event.dto;

import javax.validation.constraints.NotNull;
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
public class EventCalendarRegisterRequest {
    @NotNull
    private Long eventId;
}
