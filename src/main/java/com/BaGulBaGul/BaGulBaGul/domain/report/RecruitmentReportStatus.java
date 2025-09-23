package com.BaGulBaGul.BaGulBaGul.domain.report;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
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
@DiscriminatorValue("Recruitment")
@Getter
public class RecruitmentReportStatus extends ReportStatus {
    @Setter
    @JoinColumn(name = "recruitment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Recruitment recruitment;

    @Column(name = "active_recruitment_id", updatable = false)
    private Long activeRecruitmentId;

    @Builder
    public RecruitmentReportStatus(Recruitment recruitment) {
        this.recruitment = recruitment;
    }
}
