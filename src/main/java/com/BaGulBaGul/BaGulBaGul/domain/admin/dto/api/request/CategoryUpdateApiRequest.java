package com.BaGulBaGul.BaGulBaGul.domain.admin.dto.api.request;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.CategoryUpdateRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateApiRequest {
    @ApiModelProperty(value = "카테고리 이름")
    private String name;

    public CategoryUpdateRequest toServiceRequest() {
        return new CategoryUpdateRequest(name);
    }
}