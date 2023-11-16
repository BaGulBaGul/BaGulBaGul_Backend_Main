package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.constant.RecruitmentState;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class RecruitmentSimpleResponse {

    @ApiModelProperty(value = "모잡글 id")
    private Long id;

    @ApiModelProperty(value = "모집 상태")
    private RecruitmentState state;

    @ApiModelProperty(value = "시작 시간")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "종료 시간")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "게시글 제목")
    private String title;

    @ApiModelProperty(value = "등록자 닉네임")
    private String username;

    @ApiModelProperty(value = "태그들", example = "[\"물놀이\",\"바베큐\"]")
    private List<String> tags;

    @ApiModelProperty(value = "생성일")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "마지막 수정일")
    private LocalDateTime lastModifiedAt;

    public static RecruitmentSimpleResponse of(Recruitment recruitment) {
        return RecruitmentSimpleResponse.builder()
                .id(recruitment.getId())
                .startDate(recruitment.getStartDate())
                .endDate(recruitment.getEndDate())
                .state(recruitment.getState())
                .title(recruitment.getPost().getTitle())
                .username(recruitment.getPost().getUser().getNickname())
                .tags(Arrays.asList(recruitment.getPost().getTags().split(" ")))
                .createdAt(recruitment.getPost().getCreatedAt())
                .lastModifiedAt(recruitment.getPost().getLastModifiedAt())
                .build();
    }
}
