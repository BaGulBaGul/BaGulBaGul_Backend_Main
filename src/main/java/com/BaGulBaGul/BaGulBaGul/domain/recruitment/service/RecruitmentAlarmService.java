package com.BaGulBaGul.BaGulBaGul.domain.recruitment.service;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent.NewRecruitmentLikeApplicationEvent;

public interface RecruitmentAlarmService {
     void alarmToRecruitmentWriter(NewRecruitmentLikeApplicationEvent newRecruitmentLikeApplicationEvent);
}
