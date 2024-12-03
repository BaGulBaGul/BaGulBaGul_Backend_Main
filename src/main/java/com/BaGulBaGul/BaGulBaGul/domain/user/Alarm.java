package com.BaGulBaGul.BaGulBaGul.domain.user;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.constant.AlarmType;
import java.time.LocalDateTime;
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
import javax.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    Long id;

    @Setter
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    User user;

    @Setter
    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    AlarmType type;

    @Setter
    @Column(name = "title")
    String title;

    @Setter
    @Column(name = "message")
    String message;

    @Setter
    @Column(name = "subject")
    String subject;

    @Setter
    @Column(name = "checked")
    boolean checked;

    @Setter
    @Column(name = "time")
    LocalDateTime time;

    @Version
    Integer version;

    @Builder
    public Alarm(User user, AlarmType type, String title, String message, String subject, boolean checked, LocalDateTime time) {
        this.user = user;
        this.type = type;
        this.title = title;
        this.message = message;
        this.subject = subject;
        this.checked = checked;
        this.time = time;
    }
}
