package com.BaGulBaGul.BaGulBaGul.domain.user;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class UserAlarmStatus {
    @Id
    @Column(name = "user_id")
    Long userId;

    @MapsId
    @JoinColumn(name = "user_id")
    @OneToOne(fetch = FetchType.LAZY)
    User user;

    @Column(name = "total_alarm_count")
    @Setter
    Long totalAlarmCount;

    @Column(name = "unchecked_alarm_count")
    @Setter
    Long uncheckedAlarmCount;

    @Builder
    public UserAlarmStatus(User user, Long totalAlarmCount, Long uncheckedAlarmCount) {
        this.user = user;
        this.totalAlarmCount = totalAlarmCount;
        this.uncheckedAlarmCount = uncheckedAlarmCount;
    }
}
