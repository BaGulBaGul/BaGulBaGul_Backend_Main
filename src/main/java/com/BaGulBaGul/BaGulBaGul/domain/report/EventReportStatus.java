package com.BaGulBaGul.BaGulBaGul.domain.report;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("Event")
@Getter
public class EventReportStatus extends ReportStatus {
    @Setter
    @JoinColumn(name = "event_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    @Column(name = "active_event_id", updatable = false)
    private Long activeEventId;

    @Builder
    public EventReportStatus(Event event) {
        this.event = event;
    }
}
