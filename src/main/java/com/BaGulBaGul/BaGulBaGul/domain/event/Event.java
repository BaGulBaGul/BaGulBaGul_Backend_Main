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
import javax.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    //참여 인원수
    @Setter
    @Column(name = "headcount_current")
    Integer currentHeadCount;

    //모집 인원수
    @Setter
    @Column(name="headcount_total")
    Integer maxHeadCount;

    //세부 주소
    @Setter
    @Column(name = "location_full")
    String fullLocation;

    //시/군구 까지의 축약된 주소
    @Setter
    @Column(name = "location_abstract")
    String abstractLocation;

    //위도
    @Setter
    @Column(name = "location_latitude")
    Float latitudeLocation;

    //경도
    @Setter
    @Column(name = "location_longitude")
    Float longitudeLocation;

    //시작 시간
    @Setter
    @Column(name = "startdate")
    LocalDateTime startDate;

    //종료 시간
    @Setter
    @Column(name = "enddate")
    LocalDateTime endDate;

    //카테고리들
    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<EventCategory> categories = new ArrayList<>();

    @Builder
    public Event(
            EventType type,
            Post post,
            Integer currentHeadCount,
            Integer maxHeadCount,
            String fullLocation,
            String abstractLocation,
            Float latitudeLocation,
            Float longitudeLocation,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        this.type = type;
        this.post = post;
        this.currentHeadCount = currentHeadCount;
        this.maxHeadCount = maxHeadCount;
        this.fullLocation = fullLocation;
        this.abstractLocation = abstractLocation;
        this.latitudeLocation = latitudeLocation;
        this.longitudeLocation = longitudeLocation;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
