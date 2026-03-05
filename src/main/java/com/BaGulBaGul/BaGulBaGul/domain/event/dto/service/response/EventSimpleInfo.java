package com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EventSimpleInfo {
    private Long eventId;
    private EventType type;
    private Long eventHostUserId;
    private String eventHostUserName;
    private String eventHostUserProfileImageUrl;
    private String abstractLocation;
    private Integer currentHeadCount;
    private Integer maxHeadCount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<String> categories;
}
