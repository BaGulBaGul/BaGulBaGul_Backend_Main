package com.BaGulBaGul.BaGulBaGul.domain.event.dto;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostSimpleInfo;
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

    @ApiModelProperty(value = "이벤트 정보")
    private EventSimpleInfo event;

    @ApiModelProperty(value = "게시글 정보")
    private PostSimpleInfo post;

}
