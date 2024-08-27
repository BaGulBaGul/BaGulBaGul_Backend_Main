package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.applicationevent.NewEventCommentApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.event.applicationevent.NewEventCommentChildApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.event.applicationevent.NewEventCommentChildLikeApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.event.applicationevent.NewEventCommentLikeApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.event.applicationevent.NewEventLikeApplicationEvent;

public interface EventAlarmService {
    //이벤트에 좋아요 추가 시 이벤트 작성자에게 알람
    void alarmToEventWriter(NewEventLikeApplicationEvent newEventLikeApplicationEvent);
    //이벤트에 댓글 추가 시 이벤트 작성자에게 알람
    void alarmToEventWriter(NewEventCommentApplicationEvent newEventCommentApplicationEvent);
    //이벤트 댓글에 좋아요 추가 시 댓글 작성자에게 알람
    void alarmToEventCommentWriter(NewEventCommentLikeApplicationEvent newEventCommentLikeApplicationEvent);
    //이벤트 댓글에 대댓글 추가 시 댓글 작성자에게 알람
    void alarmToEventCommentWriter(NewEventCommentChildApplicationEvent newEventCommentChildApplicationEvent);
    //이벤트 대댓글에 좋아요 추가 시 대댓글 작성자에게 알람
    void alarmToEventCommentChildWriter(NewEventCommentChildLikeApplicationEvent newEventCommentChildLikeApplicationEvent);
    //이벤트 대댓글에 답글 작성 시 대댓글 작성자에게 알람
    void alarmToEventCommentChildWriter(NewEventCommentChildApplicationEvent newEventCommentChildApplicationEvent);

}
