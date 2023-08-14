package com.BaGulBaGul.BaGulBaGul.domain.recruitment;

import com.BaGulBaGul.BaGulBaGul.domain.base.BaseTimeEntity;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.contant.RecruitmentType;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity(name = "recruitment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruitment extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "recruitment_id")
    Long id;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    RecruitmentType type;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    User user;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Post post;

    @Column(name="title")
    String title;

    @Column(name="content")
    String content;

    @Column(name="head_count")
    Integer headCount;

    @Column(name="start_date")
    LocalDateTime startDate;

    @Column(name="end_date")
    LocalDateTime endDate;

    @Column(name="tags")
    String tags;

    @Column(name="image_uri")
    String imageURI;

    @Builder
    public Recruitment(
            RecruitmentType type,
            User user,
            Post post,
            String title,
            String content,
            Integer headCount,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String tags,
            String imageURI
    ){
        this.type = type;
        this.user = user;
        this.post = post;
        this.title = title;
        this.content = content;
        this.headCount = headCount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tags = tags;
        this.imageURI = imageURI;
    }
}
