package com.BaGulBaGul.BaGulBaGul.domain.user;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Alarm {
    @Id
    @GeneratedValue
    @Column(name = "alarm_id")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    AlarmType type;

    @Column(name = "title")
    String title;

    @Column(name = "title")
    String message;

    @Column(name = "subject_id")
    String subjectId;

    @Column(name = "checked")
    boolean checked;

    @Column(name = "time")
    LocalDateTime time;

    @Builder
    public Alarm(User user, AlarmType type, String title, String message, String subjectId, boolean checked, LocalDateTime time) {
        this.user = user;
        this.type = type;
        this.title = title;
        this.message = message;
        this.subjectId = subjectId;
        this.checked = checked;
        this.time = time;
    }
}
