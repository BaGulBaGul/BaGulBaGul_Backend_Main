package com.BaGulBaGul.BaGulBaGul.domain.event.dto;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class EventSimpleResponse {
    private Long id;
    private EventType type;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<String> tags;
    private List<String> categories;
    private String image_url;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public static EventSimpleResponse of(Event event) {
        return EventSimpleResponse.builder()
                .id(event.getId())
                .type(event.getType())
                .title(event.getPost().getTitle())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .tags(Arrays.asList(event.getPost().getTags().split(" ")))
                .categories(
                        event.getCategories()
                                .stream()
                                .map(eventCategory -> eventCategory.getCategory().getName())
                                .collect(Collectors.toList())
                )
                .image_url(event.getPost().getImage_url())
                .createdAt(event.getPost().getCreatedAt())
                .lastModifiedAt(event.getPost().getLastModifiedAt())
                .build();
    }
}
