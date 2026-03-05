package com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostWriterInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostWriterApiInfo {
    @ApiModelProperty(value = "등록자 id")
    private Long userId;

    @ApiModelProperty(value = "등록자 닉네임")
    private String userName;

    @ApiModelProperty(value = "등록자 이미지 url")
    private String userProfileImageUrl;

    public static PostWriterApiInfo from(PostWriterInfo postWriterInfo) {
        return PostWriterApiInfo.builder()
                .userId(postWriterInfo.getUserId())
                .userName(postWriterInfo.getUserName())
                .userProfileImageUrl(postWriterInfo.getUserProfileImageUrl())
                .build();
    }
}
