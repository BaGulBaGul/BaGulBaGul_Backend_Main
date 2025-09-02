package com.BaGulBaGul.BaGulBaGul.domain.admin.dto.api.event.request;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.CategoryRegisterRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRegisterApiRequest {
    @ApiModelProperty(value = "카테고리 이름")
    private String name;

    public CategoryRegisterRequest toServiceRequest() {
        return new CategoryRegisterRequest(name);
    }
}