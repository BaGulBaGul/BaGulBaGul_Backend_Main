package com.BaGulBaGul.BaGulBaGul.domain.event;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.AssertTrue;
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
    @Column(name = "deleted")
    Boolean deleted;

    @Setter
    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    EventType type;

    @Setter
    @JoinColumn(name = "event_host_user_id")
    @ManyToOne
    User hostUser;

    @Setter
    @JoinColumn(name = "post_id")
    @OneToOne(fetch = FetchType.LAZY)
    Post post;

    @Setter
    @Column(name = "age_limit")
    Boolean ageLimit;

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
    public Event(
            EventType type,
            User hostUser,
            Post post,
            Boolean ageLimit,
            Integer currentHeadCount,
            Integer maxHeadCount,
            String fullLocation,
            String abstractLocation,
            Float latitudeLocation,
            Float longitudeLocation,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        this.deleted = false;
        this.type = type;
        this.hostUser = hostUser;
        this.post = post;
        this.ageLimit = ageLimit;
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
