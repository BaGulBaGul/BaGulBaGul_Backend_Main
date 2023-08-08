package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.contant.RecruitmentType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecruitmentRequestDto {
    @Data @Builder
    public static class RInfo {

        private Long userId;

        private RecruitmentType type;

        private String title;

        private String content;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        private String tags;

        private String imageURL;

        private String category;

        private Integer likeCount;

        private Integer commentCount;

        private Integer headCount;
    }

}
