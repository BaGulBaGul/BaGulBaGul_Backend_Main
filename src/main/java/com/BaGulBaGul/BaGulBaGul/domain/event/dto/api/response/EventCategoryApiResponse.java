package com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.event.Category;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventCategoryApiResponse {
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "카테고리 이름")
    private String name;

    public static EventCategoryApiResponse of(Category category) {
        return EventCategoryApiResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
