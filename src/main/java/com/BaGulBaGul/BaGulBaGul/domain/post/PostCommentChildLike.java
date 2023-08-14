package com.BaGulBaGul.BaGulBaGul.domain.post;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(name = "UK_POST_LIKE", columnNames = {"post_comment_child_id", "user_id"}))
@Entity
public class PostCommentChildLike {

    @Id
    @GeneratedValue
    @Column(name = "post_comment_child_like_id")
    private Long id;

    @JoinColumn(name = "post_comment_child_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PostCommentChild postCommentChild;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public PostCommentChildLike(PostCommentChild postCommentChild, User user) {
        this.postCommentChild = postCommentChild;
        this.user = user;
    }
}
