package com.BaGulBaGul.BaGulBaGul.domain.report;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("Event")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class EventReport extends Report {
    @JoinColumn(name = "event_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;
}
