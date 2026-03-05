package com.BaGulBaGul.BaGulBaGul.domain.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSuspensionStatus {

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Setter
    @Column(name = "reason")
    private String reason;

    public UserSuspensionStatus(User user, LocalDateTime endDate, String reason) {
        this.user = user;
        this.endDate = endDate;
        this.reason = reason;
    }
}
