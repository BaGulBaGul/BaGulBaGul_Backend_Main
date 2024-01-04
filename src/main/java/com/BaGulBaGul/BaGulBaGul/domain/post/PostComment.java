package com.BaGulBaGul.BaGulBaGul.domain.post;

import com.BaGulBaGul.BaGulBaGul.domain.base.BaseTimeEntity;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
public class PostComment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_comment_id")
    Long id;

    @Setter
    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Post post;

    @Setter
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    User user;

    @Setter
    @Column(name = "contnet")
    String content;

    @Setter
    @Column(name = "comment_child_count")
    int commentChildCount;

    @Setter
    @Column(name = "like_count")
    int likeCount;

    @OneToMany(mappedBy = "postComment", fetch = FetchType.LAZY)
    List<PostCommentChild> children = new ArrayList<>();

    @OneToMany(mappedBy = "postComment", fetch = FetchType.LAZY)
    List<PostCommentLike> likes = new ArrayList<>();

    @Builder
    public PostComment(
            Post post,
            User user,
            String content
    ) {
        this.post = post;
        this.user = user;
        this.content = content;
        this.commentChildCount = 0;
        this.likeCount = 0;
    }
}
