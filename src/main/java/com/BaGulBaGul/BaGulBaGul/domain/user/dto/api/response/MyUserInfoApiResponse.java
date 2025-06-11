package com.BaGulBaGul.BaGulBaGul.domain.user.dto.api.response;


import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.MyUserInfoResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class MyUserInfoApiResponse {
    @ApiModelProperty(value = "유저 id")
    Long id;
    @ApiModelProperty(value = "닉네임")
    String nickname;
    @ApiModelProperty(value = "이메일")
    String email;
    @ApiModelProperty(value = "프로필 상태 메세지")
    String profileMessage;
    @ApiModelProperty(value = "프로필 이미지")
    String imageURI;
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
