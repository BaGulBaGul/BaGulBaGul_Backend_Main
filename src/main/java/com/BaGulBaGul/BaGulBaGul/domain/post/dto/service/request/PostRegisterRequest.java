package com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostRegisterRequest {
    @NotBlank(message = "제목은 null이거나 빈 문자열일 수 없습니다.")
    private String title;

    private String content;

    @Size(max = 10, message = "태그 개수는 {max}개 이하여야 합니다.")
    private List<String> tags;

    @Size(max = 10, message = "이미지 개수는 {max}개 이하여야 합니다.")
    private List<Long> imageIds;
}
