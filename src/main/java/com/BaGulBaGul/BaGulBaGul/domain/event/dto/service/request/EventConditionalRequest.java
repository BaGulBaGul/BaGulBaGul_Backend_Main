package com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request.PostConditionalRequest;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventConditionalRequest {

    //이벤트 타입 FESTIVAL, LOCAL_EVENT, PARTY 중 하나
    private EventType type;

    //요청 카테고리를 모두 가진 이벤트 검색
    private List<String> categories;

    //요청 장소를 포함하는 이벤트 검색
    private String location;

    //검색 시작 시간. 검색 종료 시간과 함께 요청해야 한다. 시작시간~검색시간 사이에 진행되는 이벤트 검색
    private LocalDateTime startDate;

    //검색 종료 시간.
    private LocalDateTime endDate;

    //남은 자리가 leftHeadCount 이상인 이벤트 검색
    private Integer leftHeadCount;

    //최소 모집 인원
    private Integer maxHeadCountMin;

    //최대 모집 인원
    private Integer maxHeadCountMax;

    //게시글 관련 검색
    @Valid
    @Builder.Default
    private PostConditionalRequest postConditionalRequest = new PostConditionalRequest();
}
