package com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostDetailInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class EventDetailResponse {

    @ApiModelProperty(value = "이벤트 정보")
    private EventDetailInfo event;

    @ApiModelProperty(value = "게시글 정보")
    private PostDetailInfo post;

}
