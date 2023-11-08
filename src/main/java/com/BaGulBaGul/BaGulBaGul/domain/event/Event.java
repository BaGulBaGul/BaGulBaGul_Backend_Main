package com.BaGulBaGul.BaGulBaGul.domain.event;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {
    @Id
    @GeneratedValue
    @Column(name = "event_id")
    Long id;

    @Setter
    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    EventType type;

    @Setter
    @JoinColumn(name = "post_id")
    @OneToOne(fetch = FetchType.LAZY)
    Post post;

    @Setter
    @Column(name = "headcount")
    Integer headCount;

    @Setter
    @Column(name = "startdate")
    LocalDateTime startDate;

    @Setter
    @Column(name = "enddate")
    LocalDateTime endDate;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<EventCategory> categories = new ArrayList<>();

    @Builder
    public Event(
            EventType type,
            Post post,
            Integer headCount,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        this.type = type;
        this.post = post;
        this.headCount = headCount;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
