package com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.request;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventConditionalRequest;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventPageApiRequest {

    @ApiModelProperty(value = "이벤트 타입 FESTIVAL, LOCAL_EVENT, PARTY 중 하나")
    private EventType type;

    @ApiModelProperty(value = "요청 제목을 포함하는 이벤트 검색")
    private String title;

    @ApiModelProperty(value = "요청 태그를 모두 가진 이벤트 검색")
    private List<String> tags;

    @ApiModelProperty(value = "요청 카테고리를 모두 가진 이벤트 검색")
    private List<String> categories;

    @ApiModelProperty(value = "작성자 닉네임이 일치하는 이벤트 검색")
    private String username;

    @ApiModelProperty(value = "요청 장소를 포함하는 이벤트 검색")
    private String location;

    @ApiModelProperty(value = "검색 시작 시간. 검색 종료 시간과 함께 요청해야 한다. 시작시간~검색시간 사이에 진행되는 이벤트 검색")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "검색 종료 시간.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "남은 인원 수")
    private Integer leftHeadCount;

    @ApiModelProperty(value = "최소 모집 인원")
    private Integer maxHeadCountMin;

    @ApiModelProperty(value = "최대 모집 인원")
    private Integer maxHeadCountMax;

    public EventConditionalRequest toEventConditionalRequest() {
        throw new UnsupportedOperationException();
    }
}
