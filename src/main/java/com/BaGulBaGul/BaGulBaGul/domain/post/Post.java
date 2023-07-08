package com.BaGulBaGul.BaGulBaGul.domain.post;

import com.BaGulBaGul.BaGulBaGul.domain.post.constant.PostType;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Entity(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    Long id;

    @Setter
    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    PostType type;

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
    @Column(name = "headcount")
    Integer headCount;

    @Setter
    @Column(name = "startdate")
    LocalDateTime startDate;

    @Setter
    @Column(name = "enddate")
    LocalDateTime endDate;

    @Setter
    @Column(name = "tags")
    String tags;

    @Setter
    @Column(name = "image_url")
    String image_url;

    @Builder
    public Post(
            PostType type,
            User user,
            String title,
            String content,
            Integer headCount,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String tags,
            String image_url
    ) {
        this.type = type;
        this.user = user;
        this.title = title;
        this.content = content;
        this.headCount = headCount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tags = tags;
        this.image_url = image_url;
    }
}
