package com.BaGulBaGul.BaGulBaGul.domain.event.dto;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class EventDetailResponse {
    private Long id;
    private EventType type;
    private Long postId;
    private String userName;
    private String title;
    private String content;
    private Integer headCount;
    private String fullLocation;
    private String abstractLocation;
    private Float latitudeLocation;
    private Float longitudeLocation;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<String> tags;
    private List<String> categories;
    private String image_url;
    private int likeCount;
    private int commentCount;
    private int views;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public static EventDetailResponse of(Event event) {
        List<String> tags = event.getPost().getTags().equals("") ?
                new ArrayList<>() :
                Arrays.asList(event.getPost().getTags().split(" ")).stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
        return EventDetailResponse.builder()
                .id(event.getId())
                .type(event.getType())
                .postId(event.getPost().getId())
                .userName(event.getPost().getUser().getNickname())
                .title(event.getPost().getTitle())
                .content(event.getPost().getContent())
                .headCount(event.getHeadCount())
                .fullLocation(event.getFullLocation())
                .abstractLocation(event.getAbstractLocation())
                .latitudeLocation(event.getLatitudeLocation())
                .longitudeLocation(event.getLongitudeLocation())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .tags(tags)
                .categories(
                        event.getCategories()
                                .stream()
                                .map(eventCategory -> eventCategory.getCategory().getName())
                                .collect(Collectors.toList())
                )
                .image_url(event.getPost().getImage_url())
                .likeCount(event.getPost().getLikeCount())
                .commentCount(event.getPost().getCommentCount())
                .views(event.getPost().getViews())
                .createdAt(event.getPost().getCreatedAt())
                .lastModifiedAt(event.getPost().getLastModifiedAt())
                .build();
    }
}
