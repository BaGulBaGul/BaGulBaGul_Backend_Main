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
public class NewRecruitmentCommentApplicationEvent extends BasicTimeApplicationEvent {
    //새로 등록된 댓글이 속한 모잡글의 id
    private Long recruitmentId;
    //새로 등록된 댓글의 id
    private Long newCommentId;
}
