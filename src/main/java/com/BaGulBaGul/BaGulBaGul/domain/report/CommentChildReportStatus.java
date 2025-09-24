package com.BaGulBaGul.BaGulBaGul.domain.report;

import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChild;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("CommentChild")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentChildReportStatus extends ReportStatus {
    //신고 대상 게시물
    @Setter
    @JoinColumn(name = "post_comment_child_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PostCommentChild postCommentChild;

    // 활성화된 신고 상태라면(PROCEEDING) 이 값은 게시물의 id가 된다. 그렇지 않다면 null이 된다.
    // 이것은 db에서 자동으로 대입한다. unique 조건을 통해 특정 게시물의 활성화된 신고 상태는 1개로 종합해서 관리한다.
    @Column(name = "active_post_comment_child_id", insertable = false, updatable = false)
    private Long activePostCommentChildId;

    @Builder
    public CommentChildReportStatus(PostCommentChild postCommentChild) {
        this.postCommentChild = postCommentChild;
    }
}
