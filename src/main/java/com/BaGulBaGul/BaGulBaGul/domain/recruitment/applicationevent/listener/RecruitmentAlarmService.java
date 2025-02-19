package com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent.listener;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent.NewRecruitmentCommentApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent.NewRecruitmentCommentChildApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent.NewRecruitmentCommentChildLikeApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent.NewRecruitmentCommentLikeApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent.NewRecruitmentLikeApplicationEvent;

public interface RecruitmentAlarmService {
     //모집글에 좋아요 추가 시 모집글 작성자에게 알람
     void alarmToRecruitmentWriter(NewRecruitmentLikeApplicationEvent newRecruitmentLikeApplicationEvent);
     //모집글에 댓글 추가 시 모집글 작성자에게 알람
     void alarmToRecruitmentWriter(NewRecruitmentCommentApplicationEvent newRecruitmentCommentApplicationEvent);
     //모집글 댓글에 좋아요 추가 시 댓글 작성자에게 알람
     void alarmToRecruitmentCommentWriter(
             NewRecruitmentCommentLikeApplicationEvent newRecruitmentCommentLikeApplicationEvent);
     //모집글 댓글에 대댓글 추가 시 댓글 작성자에게 알람
     void alarmToRecruitmentCommentWriter(
             NewRecruitmentCommentChildApplicationEvent newRecruitmentCommentChildApplicationEvent);
     //모집글 대댓글에 좋아요 추가 시 대댓글 작성자에게 알람
     void alarmToRecruitmentCommentChildWriter(
             NewRecruitmentCommentChildLikeApplicationEvent newRecruitmentCommentChildLikeApplicationEvent);
     //모집글 대댓글에 답글 작성 시 대댓글 작성자에게 알람
     void alarmToRecruitmentCommentChildWriter(
             NewRecruitmentCommentChildApplicationEvent newRecruitmentCommentChildApplicationEvent);
}
