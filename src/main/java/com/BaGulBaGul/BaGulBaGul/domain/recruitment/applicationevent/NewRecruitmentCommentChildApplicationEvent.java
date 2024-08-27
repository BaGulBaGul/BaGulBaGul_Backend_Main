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
public class NewRecruitmentCommentChildApplicationEvent extends BasicTimeApplicationEvent {
    //새로 등록된 대댓글의 id
    private Long newCommentChildId;
    //답장 대상 대댓글의 id
    private Long replyTargetCommentChildId;
}
