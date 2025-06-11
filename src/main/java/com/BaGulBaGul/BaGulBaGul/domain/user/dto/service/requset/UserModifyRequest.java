package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class UserModifyRequest {
    @Builder.Default
    JsonNullable<String> email = JsonNullable.undefined();

    @Builder.Default
    JsonNullable<String> profileMessage = JsonNullable.undefined();

    @Builder.Default
    JsonNullable<Long> imageResourceId = JsonNullable.undefined();

    @Builder.Default
    JsonNullable<String> username = JsonNullable.undefined();
}
