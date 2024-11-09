package com.BaGulBaGul.BaGulBaGul.domain.report;

import com.BaGulBaGul.BaGulBaGul.domain.base.BaseTimeEntity;
import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportType;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Report extends BaseTimeEntity {
    //신고 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    //신고 종류
    @Column(name = "report_type")
    @Enumerated(value = EnumType.STRING)
    private ReportType reportType;

    //처리(해결) 여부
    @Column(name = "solved")
    private boolean solved;

    //신고 메세지
    @Column(name = "message")
    private String message;

    //신고된 유저 id
    @JoinColumn(name = "reported_user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User reportedUser;

    //신고자 유저 id
    @JoinColumn(name = "reporting_user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User reportingUser;

    protected Report(ReportBuilder<?, ?> b) {
        this.reportType = b.reportType;
        this.message = b.message;
        this.reportedUser = b.reportedUser;
        this.reportingUser = b.reportingUser;
    }

    public static abstract class ReportBuilder<C extends Report, B extends ReportBuilder<C, B>> {
        private ReportType reportType;
        private String message;
        private User reportedUser;
        private User reportingUser;

        public B reportType(ReportType reportType) {
            this.reportType = reportType;
            return self();
        }

        public B message(String message) {
            this.message = message;
            return self();
        }

        public B reportedUser(User reportedUser) {
            this.reportedUser = reportedUser;
            return self();
        }

        public B reportingUser(User reportingUser) {
            this.reportingUser = reportingUser;
            return self();
        }

        protected abstract B self();

        public abstract C build();
    }
}
