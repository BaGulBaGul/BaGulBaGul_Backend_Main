package com.BaGulBaGul.BaGulBaGul.domain.event.dto;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class GetLikeEventRequest {
    EventType type;
}
