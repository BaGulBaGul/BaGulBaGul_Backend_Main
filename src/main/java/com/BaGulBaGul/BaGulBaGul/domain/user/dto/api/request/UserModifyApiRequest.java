package com.BaGulBaGul.BaGulBaGul.domain.user.dto.api.request;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.UserModifyRequest;
import io.swagger.annotations.ApiModelProperty;
import java.util.regex.Pattern;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserModifyApiRequest {
    @ApiModelProperty(value = "이메일")
    @Builder.Default
    JsonNullable<String> email = JsonNullable.undefined();

    @ApiModelProperty(value = "프로필 상태 메세지")
    @Builder.Default
    JsonNullable<String> profileMessage = JsonNullable.undefined();

    @ApiModelProperty(value = "프로필 이미지의 resource id")
    @Builder.Default
    JsonNullable<Long> imageResourceId = JsonNullable.undefined();

    @ApiModelProperty(value = "유저명")
    @Builder.Default
    JsonNullable<String> username = JsonNullable.undefined();


    public UserModifyRequest toUserModifyRequest() {
        return UserModifyRequest.builder()
                .email(email)
                .profileMessage(profileMessage)
                .imageResourceId(imageResourceId)
                .username(username)
                .build();
    }
}
