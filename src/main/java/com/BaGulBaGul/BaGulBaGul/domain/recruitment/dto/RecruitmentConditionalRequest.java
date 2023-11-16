package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostConditionalRequest;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class RecruitmentConditionalRequest {

    @ApiModelProperty(value = "요청 제목을 포함하는 모집글 검색")
    private String title;

    @ApiModelProperty(value = "요청 태그를 모두 가진 모집글 검색")
    private List<String> tags;

    @ApiModelProperty(value = "작성자 닉네임이 일치하는 모집글 검색")
    private String username;

    @ApiModelProperty(value = "이벤트 id가 일치하는 모집글 검색")
    private Long eventId;

    public PostConditionalRequest toPostConditionalRequest() {
        return PostConditionalRequest.builder()
                .title(title)
                .username(username)
                .tags(tags)
                .build();
    }
}
