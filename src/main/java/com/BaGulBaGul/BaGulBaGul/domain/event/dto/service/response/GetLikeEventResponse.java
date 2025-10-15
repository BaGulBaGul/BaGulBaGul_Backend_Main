package com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import io.swagger.annotations.ApiModelProperty;
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

    private Long eventId;
    private String title;
    private String headImageUrl;
    private String abstractLocation;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long eventWriterId;
    private String eventWriterProfileImageUrl;

    public static GetLikeEventResponse of(Event event) {
        return GetLikeEventResponse.builder()
                .eventId(event.getId())
                .title(event.getPost().getTitle())
                .headImageUrl(event.getPost().getImage_url())
                .abstractLocation(event.getAbstractLocation())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .eventWriterId(event.getPost().getUser().getId())
                .eventWriterProfileImageUrl(event.getPost().getUser().getImageURI())
                .build();
    }
}
