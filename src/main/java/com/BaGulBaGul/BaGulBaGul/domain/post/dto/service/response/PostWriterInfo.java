package com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostWriterInfo {
    private Long userId;

    private String userName;

    private String userProfileImageUrl;

    public static PostWriterInfo of(User writer) {
        if(writer == null) {
            return PostWriterInfo.builder()
                    .userId(null)
                    .userName(null)
                    .userProfileImageUrl(null)
                    .build();
        }
        else {
            return PostWriterInfo.builder()
                    .userId(writer.getId())
                    .userName(writer.getNickname())
                    .userProfileImageUrl(writer.getImageURI())
                    .build();
        }
    }
}
