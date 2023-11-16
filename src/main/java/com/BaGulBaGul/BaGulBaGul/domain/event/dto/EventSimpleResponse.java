package com.BaGulBaGul.BaGulBaGul.domain.event.dto;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "이벤트 id")
    private Long id;

    @ApiModelProperty(value = "이벤트 타입 FESTIVAL, LOCAL_EVENT, PARTY 중 하나")
    private EventType type;

    @ApiModelProperty(value = "게시글 제목")
    private String title;

    @ApiModelProperty(value = "요약 주소", example = "서울시 영등포구")
    private String abstractLocation;

    @ApiModelProperty(value = "시작 시간")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "종료 시간")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "태그들", example = "[\"물놀이\",\"바베큐\"]")
    private List<String> tags;

    @ApiModelProperty(value = "카테고리들", example = "[\"스포츠/레저\",\"식품/음료\",\"문화/예술\"]")
    private List<String> categories;

    @ApiModelProperty(value = "대표이미지 경로")
    private String image_url;

    @ApiModelProperty(value = "생성일")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "마지막 수정일")
    private LocalDateTime lastModifiedAt;

    public static EventSimpleResponse of(Event event) {
        return EventSimpleResponse.builder()
                .id(event.getId())
                .type(event.getType())
                .title(event.getPost().getTitle())
                .abstractLocation(event.getAbstractLocation())
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
