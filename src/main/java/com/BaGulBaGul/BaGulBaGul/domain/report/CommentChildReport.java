package com.BaGulBaGul.BaGulBaGul.domain.report;

import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChild;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("CommentChild")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class CommentChildReport extends Report {
    @JoinColumn(name = "post_comment_child_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PostCommentChild postCommentChild;
}
