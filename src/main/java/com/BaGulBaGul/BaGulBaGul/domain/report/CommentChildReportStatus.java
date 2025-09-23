package com.BaGulBaGul.BaGulBaGul.domain.report;

import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChild;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("CommentChild")
@Getter
public class CommentChildReportStatus extends ReportStatus {
    @Setter
    @JoinColumn(name = "post_comment_child_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PostCommentChild postCommentChild;

    @Column(name = "active_post_comment_child_id", updatable = false)
    private Long activePostCommentChildId;

    @Builder
    public CommentChildReportStatus(PostCommentChild postCommentChild) {
        this.postCommentChild = postCommentChild;
    }
}
