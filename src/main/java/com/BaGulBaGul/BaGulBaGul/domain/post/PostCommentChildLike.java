package com.BaGulBaGul.BaGulBaGul.domain.post;

import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChildLike.PostCommentChildLikeId;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "post_comment_child_like")
@IdClass(PostCommentChildLikeId.class)
public class PostCommentChildLike {
    @EqualsAndHashCode
    public static class PostCommentChildLikeId implements Serializable {
        private Long postCommentChild;
        private Long user;
    }

    @Id
    @JoinColumn(name = "post_comment_child_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PostCommentChild postCommentChild;

    @Id
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public PostCommentChildLike(PostCommentChild postCommentChild, User user) {
        this.postCommentChild = postCommentChild;
        this.user = user;
    }
}
