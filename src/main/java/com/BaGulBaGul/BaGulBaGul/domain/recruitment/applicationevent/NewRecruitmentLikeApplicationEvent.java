package com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent;

import com.BaGulBaGul.BaGulBaGul.global.event.BasicTimeEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class NewRecruitmentLikeApplicationEvent extends BasicTimeEvent {
    //좋아요를 받은 모집글의 id
    private Long recruitmentId;
    //좋아요를 누른 유저의 id
    private Long userId;
}