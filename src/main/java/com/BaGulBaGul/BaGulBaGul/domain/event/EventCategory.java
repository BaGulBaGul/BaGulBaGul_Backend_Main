package com.BaGulBaGul.BaGulBaGul.domain.event;

import com.BaGulBaGul.BaGulBaGul.domain.event.EventCategory.EventCategoryId;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Entity
@IdClass(EventCategoryId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventCategory {
    @EqualsAndHashCode
    public static class EventCategoryId implements Serializable {
        Long event;
        Long category;
    }

    @Id
    @JoinColumn(name = "event_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Event event;

    @Id
    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Category category;

    public EventCategory(
            Event event,
            Category category
    ) {
        this.event = event;
        this.category = category;
    }
}
