package com.BaGulBaGul.BaGulBaGul.domain.recruitment;

import com.BaGulBaGul.BaGulBaGul.domain.base.BaseTimeEntity;
import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.constant.RecruitmentState;
import java.time.LocalDateTime;
import lombok.*;
import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruitment extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "recruitment_id")
    Long id;

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
    @Column(name="head_count")
    Integer headCount;

    @Setter
    @Column(name = "startdate")
    LocalDateTime startDate;

    @Setter
    @Column(name = "enddate")
    LocalDateTime endDate;

    @Builder
    public Recruitment(
            Event event,
            Post post,
            Integer headCount,
            LocalDateTime startDate,
            LocalDateTime endDate
    ){
        this.event = event;
        this.post = post;
        this.headCount = headCount;
        this.startDate = startDate;
        this.endDate = endDate;

        this.state = RecruitmentState.PROCEEDING;
    }
}
