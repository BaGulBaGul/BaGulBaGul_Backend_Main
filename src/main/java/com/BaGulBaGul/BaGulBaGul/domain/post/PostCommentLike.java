package com.BaGulBaGul.BaGulBaGul.domain.post;


import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentLike.PostCommentLikeId;
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
@Entity
@IdClass(PostCommentLikeId.class)
public class PostCommentLike {
    @EqualsAndHashCode
    public static class PostCommentLikeId implements Serializable {
        private Long postComment;
        private Long user;
    }

    @Id
    @JoinColumn(name = "post_comment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PostComment postComment;

    @Id
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public PostCommentLike(PostComment postComment, User user) {
        this.postComment = postComment;
        this.user = user;
    }
}
