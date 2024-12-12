package com.BaGulBaGul.BaGulBaGul.domain.recruitment;

import com.BaGulBaGul.BaGulBaGul.domain.base.BaseTimeEntity;
import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.constant.RecruitmentState;
import java.time.LocalDateTime;
import javax.validation.constraints.AssertTrue;
import lombok.*;
import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruitment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruitment_id")
    Long id;

    @Setter
    @Column(name = "deleted")
    Boolean deleted;

    @Setter
    @Column(name = "state")
    RecruitmentState state;

    @Setter
    @JoinColumn(name = "event_id")
    @OneToOne(fetch = FetchType.LAZY)
    Event event;

    @Setter
    @JoinColumn(name = "post_id")
    @OneToOne(fetch = FetchType.LAZY)
    Post post;

    @Setter
    @Column(name="headcount_current")
    Integer currentHeadCount;

    @Setter
    @Column(name="headcount_total")
    Integer maxHeadCount;

    @Setter
    @Column(name = "startdate")
    LocalDateTime startDate;

    @Setter
    @Column(name = "enddate")
    LocalDateTime endDate;

    @AssertTrue(message = "시작 일시는 종료 일시보다 빨라야 합니다.")
    private boolean isStartDateBeforeEndDate() {
        return startDate == null || endDate == null || startDate.isBefore(endDate);
    }

    @AssertTrue(message = "현재 인원이 모집 인원보다 클 수 없습니다")
    private boolean isCurrentHeadCount_LessThanOrEqual_MaxHeadCount() {
        //둘 중 하나라도 null일 경우 비교 무시
        return currentHeadCount == null || maxHeadCount == null || currentHeadCount <= maxHeadCount;
    }

    @Builder
    public Recruitment(
            Event event,
            Post post,
            Integer currentHeadCount,
            Integer maxHeadCount,
            LocalDateTime startDate,
            LocalDateTime endDate
    ){
        this.deleted = false;
        this.event = event;
        this.post = post;
        this.currentHeadCount = currentHeadCount;
        this.maxHeadCount = maxHeadCount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = RecruitmentState.PROCEEDING;
    }
}
