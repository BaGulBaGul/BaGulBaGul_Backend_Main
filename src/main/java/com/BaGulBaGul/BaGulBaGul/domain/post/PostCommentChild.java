package com.BaGulBaGul.BaGulBaGul.domain.post;

import com.BaGulBaGul.BaGulBaGul.domain.base.BaseTimeEntity;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCommentChild extends BaseTimeEntity {
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

    @Setter
    @Column(name = "like_count")
    int likeCount;

    @OneToMany(mappedBy = "postCommentChild", fetch = FetchType.LAZY)
    List<PostCommentChildLike> likes = new ArrayList<>();

    @Builder
    public PostCommentChild(
            PostComment postComment,
            User user,
            String content
    ) {
        this.postComment = postComment;
        this.user = user;
        this.content = content;
        this.likeCount = 0;
    }
}
