package com.BaGulBaGul.BaGulBaGul.domain.user;

import com.BaGulBaGul.BaGulBaGul.domain.base.BaseTimeEntity;
import com.BaGulBaGul.BaGulBaGul.domain.user.constant.UserSuspensionActionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSuspensionLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_suspension_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "reason")
    private String reason;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private User admin;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private UserSuspensionActionType actionType;

    public UserSuspensionLog(User user, String reason, LocalDateTime startDate, LocalDateTime endDate, User admin, UserSuspensionActionType actionType) {
        this.user = user;
        this.reason = reason;
        this.startDate = startDate;
        this.endDate = endDate;
        this.admin = admin;
        this.actionType = actionType;
    }
}
