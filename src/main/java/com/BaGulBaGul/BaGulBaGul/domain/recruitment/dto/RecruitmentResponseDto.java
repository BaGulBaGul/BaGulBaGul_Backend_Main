package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto;

import com.BaGulBaGul.BaGulBaGul.domain.base.BaseTimeEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecruitmentResponseDto {
    @Data @Builder
    public static class RInfo {

        private Long recruitmentId;

        private String title;

        private String content;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        private String tags;

        private String imageURL;

        private String category;

        private Integer likeCount;

        private Integer commentCount;

        private LocalDateTime createdAt;

        private LocalDateTime lastModifiedAt;
    }

    @Data @Builder
    public static class RInfoWithPaging{

        private Long recruitmentId;

        private String userImageURL;

        private String title;

        private String content;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        private String tags;

        private LocalDateTime createdAt;

        private LocalDateTime lastModifiedAt;
    }
}
