package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostRegisterRequest;
import io.swagger.annotations.ApiModelProperty;
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
public class RecruitmentRegisterRequest {

    @ApiModelProperty(value = "인원")
    private int headCount;

    @ApiModelProperty(value = "시작 시간")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "종료 시간")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "게시글 제목")
    @NotBlank
    private String title;

    @ApiModelProperty(value = "게시글 내용")
    private String content;

    @ApiModelProperty(value = "태그들", example = "[\"물놀이\",\"바베큐\"]")
    private List<String> tags;

    @ApiModelProperty(value = "대표이미지 경로")
    private String image_url;

    public PostRegisterRequest toPostRegisterRequest() {
        return PostRegisterRequest.builder()
                .title(title)
                .content(content)
                .tags(tags)
                .image_url(image_url)
                .build();
    }
}
