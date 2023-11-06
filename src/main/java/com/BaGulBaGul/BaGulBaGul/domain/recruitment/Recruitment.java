package com.BaGulBaGul.BaGulBaGul.domain.recruitment;

import com.BaGulBaGul.BaGulBaGul.domain.base.BaseTimeEntity;
import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity(name = "recruitment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruitment extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "recruitment_id")
    Long id;

    @JoinColumn(name = "event_id")
    @OneToOne(fetch = FetchType.LAZY)
    Event event;

    @JoinColumn(name = "post_id")
    @OneToOne(fetch = FetchType.LAZY)
    Post post;

    @Column(name="head_count")
    Integer headCount;

    @Column(name="start_date")
    LocalDateTime startDate;

    @Column(name="end_date")
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
    }
}
