package com.BaGulBaGul.BaGulBaGul.domain.post;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity(name = "post_comment_child")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCommentChild {
    @Id
    @GeneratedValue
    @Column(name = "post_comment_child_id")
    Long id;

    @Setter
    @JoinColumn(name = "post_comment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    PostComment postComment;

    @Setter
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    User user;

    @Setter
    @Column(name = "content")
    String content;

    @Builder
    public PostCommentChild(
            PostComment postComment,
            User user,
            String content
    ) {
        this.postComment = postComment;
        this.user = user;
        this.content = content;
    }
}
