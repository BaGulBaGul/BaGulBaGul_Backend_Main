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
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    Long id;

    @Setter
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    User user;

    @Setter
    @Column(name = "title")
    String title;

    @Setter
    @Column(name = "content")
    String content;

    @Setter
    @Column(name = "tags")
    String tags;

    @Setter
    @Column(name = "image_url")
    String image_url;

    @Setter
    @Column(name = "like_count")
    Integer likeCount;

    @Setter
    @Column(name = "comment_count")
    Integer commentCount;

    @Setter
    @Column(name = "views")
    Integer views;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<PostLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<PostComment> comments = new ArrayList<>();

    @Builder
    public Post(
            User user,
            String title,
            String content,
            String tags,
            String image_url,
            Integer likeCount,
            Integer commentCount,
            Integer views
    ) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.image_url = image_url;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.views = views;
    }
}
