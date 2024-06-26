package com.BaGulBaGul.BaGulBaGul.global.upload.dto;

import io.swagger.annotations.ApiModelProperty;
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
public class UploadResponse {
    @ApiModelProperty(value = "resource id")
    private Long resourceId;
    @ApiModelProperty(value = "리소스 접근 경로")
    private String url;
}
