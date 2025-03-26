package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.request;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request.PostConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentConditionalRequest;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentPageApiRequest {

    @ApiModelProperty(value = "모집글이 속한 이벤트의 id")
    private Long eventId;

    @ApiModelProperty(value = "남은 인원 수")
    private Integer leftHeadCount;

    @ApiModelProperty(value = "게시글 제목")
    private String title;

    @ApiModelProperty(value = "태그들", example = "[\"물놀이\",\"바베큐\"]")
    private List<String> tags;

    @ApiModelProperty(value = "등록자 닉네임")
    private String username;

    public RecruitmentConditionalRequest toRecruitmentConditionalRequest() {
        return RecruitmentConditionalRequest.builder()
                .eventId(eventId)
                .leftHeadCount(leftHeadCount)
                .postConditionalRequest(PostConditionalRequest.builder()
                        .title(title)
                        .username(username)
                        .tags(tags)
                        .build())
                .build();
    }
}
