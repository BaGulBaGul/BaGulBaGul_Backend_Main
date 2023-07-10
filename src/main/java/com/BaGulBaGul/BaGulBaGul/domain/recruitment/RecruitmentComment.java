package com.BaGulBaGul.BaGulBaGul.domain.recruitment;

import com.BaGulBaGul.BaGulBaGul.domain.base.BaseTimeEntity;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Getter
@Entity(name="recruitment_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentComment extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "recruitment_comment_id")
    Long id;

    @JoinColumn(name = "recruitment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Recruitment recruitment;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    User user;

    @Column(name="content")
    String content;

    @Builder
    public RecruitmentComment(
            Recruitment recruitment,
            User user,
            String content
    ) {
        this.recruitment = recruitment;
        this.user = user;
        this.content = content;
    }
}
