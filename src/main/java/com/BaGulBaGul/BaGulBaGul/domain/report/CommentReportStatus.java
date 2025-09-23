package com.BaGulBaGul.BaGulBaGul.domain.report;

import com.BaGulBaGul.BaGulBaGul.domain.post.PostComment;
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
@DiscriminatorValue("Comment")
@Getter
public class CommentReportStatus extends ReportStatus {
    @Setter
    @JoinColumn(name = "post_comment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PostComment postComment;

    @Column(name = "active_post_comment_id", updatable = false)
    private Long activePostCommentId;

    @Builder
    public CommentReportStatus(PostComment postComment) {
        this.postComment = postComment;
    }
}
