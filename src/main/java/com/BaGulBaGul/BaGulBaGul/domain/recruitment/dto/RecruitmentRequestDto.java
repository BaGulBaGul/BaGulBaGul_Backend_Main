package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecruitmentRequestDto {
    @Data @Builder
    public static class RInfo {
        private String title;

        private String content;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        private String tags;

        private String imageURL;

        private String category;

        private Integer likeCount;

        private Integer commentCount;
    }
}
