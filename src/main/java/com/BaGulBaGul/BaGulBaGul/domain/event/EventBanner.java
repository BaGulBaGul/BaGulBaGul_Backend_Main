package com.BaGulBaGul.BaGulBaGul.domain.event;

import com.BaGulBaGul.BaGulBaGul.domain.upload.Resource;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventBanner {
    @Id
    @Column(name = "event_banner_id")
    Long id;

    @Setter
    @JoinColumn(name = "event_id")
    @OneToOne(fetch = FetchType.LAZY)
    Event event;

    @Setter
    @JoinColumn(name = "event_banner_image_resource_id")
    @OneToOne(fetch = FetchType.LAZY)
    Resource bannerImageResource;

    @Builder
    public EventBanner(Long id, Event event, Resource bannerImageResource) {
        this.id = id;
        this.event = event;
        this.bannerImageResource = bannerImageResource;
    }
}
