package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.constant.RecruitmentState;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RecruitmentModifyRequest {
    private RecruitmentState state;
    private Integer headCount;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;
    @NotBlank
    private String title;
    private String content;
    private List<String> tags;
    private String image_url;

    public PostModifyRequest toPostModifyRequest() {
        return PostModifyRequest.builder()
                .title(title)
                .content(content)
                .tags(tags)
                .image_url(image_url)
                .build();
    }
}
