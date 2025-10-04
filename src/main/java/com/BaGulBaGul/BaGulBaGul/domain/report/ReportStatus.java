package com.BaGulBaGul.BaGulBaGul.domain.report;

import com.BaGulBaGul.BaGulBaGul.domain.base.BaseTimeEntity;
import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportStatusState;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
public class ReportStatus extends BaseTimeEntity {
    //id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_status_id")
    private Long id;

    @Column(name = "dtype", insertable = false, updatable = false)
    private String dType;

    //처리 상태(진행중, 수락됨, 취소됨)
    @Setter
    @Column(name = "state")
    @Enumerated(value = EnumType.STRING)
    private ReportStatusState state;

    //총 신고 개수
    @Setter
    @Column(name = "total_report_count")
    private int totalReportCount;

    //관련없는 게시물 신고 개수
    @Setter
    @Column(name = "not_relevant_report_count")
    private int notRelevantReportCount;

    //공격적인 게시물 신고 개수
    @Setter
    @Column(name = "offensive_content_report_count")
    private int offensiveContentReportCount;

    //명예훼손성 게시물 신고 개수
    @Setter
    @Column(name = "defamatory_report_count")
    private int defamatoryReportCount;

    //기타 이유 신고 개수
    @Setter
    @Column(name = "ect_report_count")
    private int ectReportCount;

    //신고 처리 시 대상 게시물을 삭제했는지
    @Setter
    @Column(name = "is_reported_content_deleted")
    private boolean reportedContentDeleted;

    //신고 처리 시 대상 게시물 작성자를 정지시켰는지
    @Setter
    @Column(name = "is_reported_content_writer_suspended")
    private boolean reportedContentWriterSuspended;

    protected ReportStatus() {
        this.state = ReportStatusState.PROCEEDING;
        this.totalReportCount = 0;
        this.notRelevantReportCount = 0;
        this.defamatoryReportCount = 0;
        this.ectReportCount = 0;
        this.reportedContentDeleted = false;
    }
}
