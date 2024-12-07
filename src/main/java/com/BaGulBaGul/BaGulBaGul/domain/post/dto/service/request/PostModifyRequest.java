package com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request;

import java.util.List;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostModifyRequest {

    private String title;

    private String content;

    @Size(max = 10, message = "태그 개수는 {max}개 이하여야 합니다.")
    private List<String> tags;

    @Size(max = 10, message = "이미지 개수는 {max}개 이하여야 합니다.")
    private List<Long> imageIds;

    @AssertTrue(message = "제목은 1글자 이상이여야 합니다.")
    private boolean isTitleNotBlank() {
        return title == null || title.length() > 0;
    }
}
