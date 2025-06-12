package com.BaGulBaGul.BaGulBaGul.domain.user.dto.api.response;


import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.MyUserInfoResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
public class MyUserInfoApiResponse extends UserInfoApiResponse {
    @ApiModelProperty(value = "작성한 게시글 개수")
    long writingCount;
    @ApiModelProperty(value = "좋아요 누른 게시글 개수")
    long postLikeCount;
    @ApiModelProperty(value = "켈린더 개수")
    long calendarCount;

    public static MyUserInfoApiResponse from(MyUserInfoResponse myUserInfoResponse) {
        return MyUserInfoApiResponse.builder()
                .id(myUserInfoResponse.getId())
                .nickname(myUserInfoResponse.getNickname())
                .email(myUserInfoResponse.getEmail())
                .profileMessage(myUserInfoResponse.getProfileMessage())
                .imageURI(myUserInfoResponse.getImageURI())
                .writingCount(myUserInfoResponse.getWritingCount())
                .postLikeCount(myUserInfoResponse.getPostLikeCount())
                .calendarCount(myUserInfoResponse.getCalendarCount())
                .build();
    }
}
