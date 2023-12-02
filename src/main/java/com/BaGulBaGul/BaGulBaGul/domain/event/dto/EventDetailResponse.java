package com.BaGulBaGul.BaGulBaGul.domain.event.dto;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "이벤트 id")
    private Long id;

    @ApiModelProperty(value = "이벤트 타입 FESTIVAL, LOCAL_EVENT, PARTY 중 하나")
    private EventType type;

    @ApiModelProperty(value = "게시글 id")
    private Long postId;

    @ApiModelProperty(value = "등록자 닉네임")
    private String userName;

    @ApiModelProperty(value = "게시글 제목")
    private String title;

    @ApiModelProperty(value = "게시글 내용")
    private String content;

    @ApiModelProperty(value = "인원")
    private Integer headCount;

    @ApiModelProperty(value = "세부 주소", example = "서울시 영등포구 xxx로 xxx타워 x층")
    private String fullLocation;

    @ApiModelProperty(value = "요약 주소", example = "서울시 영등포구")
    private String abstractLocation;

    @ApiModelProperty(value = "위도")
    private Float latitudeLocation;

    @ApiModelProperty(value = "경도")
    private Float longitudeLocation;

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

    private List<String> images;

    @ApiModelProperty(value = "종아요 수")
    private int likeCount;

    @ApiModelProperty(value = "댓글 수")
    private int commentCount;

    @ApiModelProperty(value = "조회 수")
    private int views;

    @ApiModelProperty(value = "생성일")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "마지막 수정일")
    private LocalDateTime lastModifiedAt;

    public static EventDetailResponse of(Event event, List<String> imageUrls) {
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
                .images(imageUrls)
                .likeCount(event.getPost().getLikeCount())
                .commentCount(event.getPost().getCommentCount())
                .views(event.getPost().getViews())
                .createdAt(event.getPost().getCreatedAt())
                .lastModifiedAt(event.getPost().getLastModifiedAt())
                .build();
    }
}
