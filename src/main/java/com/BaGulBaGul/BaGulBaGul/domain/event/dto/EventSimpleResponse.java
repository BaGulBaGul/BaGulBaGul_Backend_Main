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

    @ApiModelProperty(value = "등록자 닉네임")
    private String userName;

    @ApiModelProperty(value = "등록자 이미지 url")
    private String userImage;

    @ApiModelProperty(value = "게시글 제목")
    private String title;

    @ApiModelProperty(value = "요약 주소", example = "서울시 영등포구")
    private String abstractLocation;

    @ApiModelProperty(value = "참여 인원")
    private Integer currentHeadCount;

    @ApiModelProperty(value = "모집 인원")
    private Integer totalHeadCount;

    @ApiModelProperty(value = "시작 시간")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "종료 시간")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "태그들", example = "[\"물놀이\",\"바베큐\"]")
    private List<String> tags;

    @ApiModelProperty(value = "카테고리들", example = "[\"스포츠/레저\",\"식품/음료\",\"문화/예술\"]")
    private List<String> categories;

    @ApiModelProperty(value = "대표이미지 경로")
    private String headImageUrl;

    @ApiModelProperty(value = "생성일")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "마지막 수정일")
    private LocalDateTime lastModifiedAt;

    public static EventSimpleResponse of(Event event) {
        return EventSimpleResponse.builder()
                .id(event.getId())
                .type(event.getType())
                .userName(event.getPost().getUser().getNickname())
                .userImage(event.getPost().getUser().getImageURI())
                .title(event.getPost().getTitle())
                .abstractLocation(event.getAbstractLocation())
                .currentHeadCount(event.getCurrentHeadCount())
                .totalHeadCount(event.getTotalHeadCount())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .tags(Arrays.asList(event.getPost().getTags().split(" ")))
                .categories(
                        event.getCategories()
                                .stream()
                                .map(eventCategory -> eventCategory.getCategory().getName())
                                .collect(Collectors.toList())
                )
                .headImageUrl(event.getPost().getImage_url())
                .createdAt(event.getPost().getCreatedAt())
                .lastModifiedAt(event.getPost().getLastModifiedAt())
                .build();
    }
}
