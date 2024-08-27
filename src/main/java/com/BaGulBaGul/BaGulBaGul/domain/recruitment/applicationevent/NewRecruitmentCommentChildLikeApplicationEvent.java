package com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent;

import com.BaGulBaGul.BaGulBaGul.global.applicationevent.BasicTimeApplicationEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class NewRecruitmentCommentChildLikeApplicationEvent extends BasicTimeApplicationEvent {
    //좋아요를 받은 대댓글의 id
    private Long likedCommentChildId;
}
