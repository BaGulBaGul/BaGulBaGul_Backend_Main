package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.contant.RecruitmentType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecruitmentRequestDto {
    @Data @Builder
    public static class RInfo {

        @ApiModelProperty(value = "유저 Id", example = "1L")
        private Long userId;

        @ApiModelProperty(value = "모집글 타입", example = "FESTIVAL")
        private RecruitmentType type;

        @ApiModelProperty(value = "모집글 제목", example = "모집글 제목 예시")
        private String title;

        @ApiModelProperty(value = "모집글 내용", example = "모집글 내용 예시")
        private String content;

        @ApiModelProperty(value = "시작 날짜", example = "2023-08-09T18:10:43.78")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/seoul")
        private LocalDateTime startDate;

        @ApiModelProperty(value = "끝나는 날짜", example = "2023-08-09T18:10:43.78")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/seoul")
        private LocalDateTime endDate;

        @ApiModelProperty(value = "해시 태그", example = "#박재범 #볼사")
        private String tags;

        @ApiModelProperty(value = "모집글 사진 URI", example = "사진 uri..")
        private String imageURI;

        @ApiModelProperty(value = "모집글 관한 게시글 카테고리" , example = "공연전시/행사")
        private String category;

        @ApiModelProperty(value = "좋아요 개수", example = "10")
        private Integer likeCount;

        @ApiModelProperty(value = "댓글 개수", example = "10")
        private Integer commentCount;

        @ApiModelProperty(value = "인원 수", example = "5")
        private Integer headCount;
    }

}
