package com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserModifyRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AMEHUserUpdateApiRequest {
    @Builder.Default
    JsonNullable<String> email = JsonNullable.undefined();

    @Builder.Default
    JsonNullable<String> profileMessage = JsonNullable.undefined();

    @Builder.Default
    JsonNullable<Long> imageResourceId = JsonNullable.undefined();

    @Builder.Default
    JsonNullable<String> username = JsonNullable.undefined();

    public UserModifyRequest toUserModifyRequest() {
        return UserModifyRequest.builder()
                .username(username)
                .email(email)
                .profileMessage(profileMessage)
                .imageResourceId(imageResourceId)
                .build();
    }
}
