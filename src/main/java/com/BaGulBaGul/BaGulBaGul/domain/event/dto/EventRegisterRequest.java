package com.BaGulBaGul.BaGulBaGul.domain.event.dto;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostRegisterRequest;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EventRegisterRequest {

    @ApiModelProperty(value = "이벤트 타입 FESTIVAL, LOCAL_EVENT, PARTY 중 하나 | 필수")
    @NotNull
    private EventType type;

    @ApiModelProperty(value = "이벤트 제목 | 필수, 공백 불허")
    @NotBlank
    private String title;

    @ApiModelProperty(value = "인원")
    private int headCount;

    @ApiModelProperty(value = "세부 주소", example = "서울시 영등포구 xxx로 xxx타워 x층")
    private String fullLocation;

    @ApiModelProperty(value = "요약 주소", example = "서울시 영등포구")
    private String abstractLocation;

    @ApiModelProperty(value = "위도")
    private Float latitudeLocation;

    @ApiModelProperty(value = "경도")
    private Float longitudeLocation;

    @ApiModelProperty(value = "게시글 내용")
    private String content;

    @ApiModelProperty(value = "시작 시간")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "종료 시간")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "태그들", example = "[\"물놀이\",\"바베큐\"]")
    private List<String> tags;

    @ApiModelProperty(value = "등록할 카테고리의 이름들", example = "[\"스포츠/레저\",\"식품/음료\",\"문화/예술\"]")
    private List<String> categories;

    @ApiModelProperty(value = "등록한 이미지들의 resource id. 순서는 보존되며 첫번째 이미지가 대표이미지가 된다.")
    @Size(max = 10)
    private List<Long> imageIds;

    public PostRegisterRequest toPostRegisterRequest() {
        return PostRegisterRequest.builder()
                .title(title)
                .content(content)
                .tags(tags)
                .imageIds(imageIds)
                .build();
    }
}
