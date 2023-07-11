package com.BaGulBaGul.BaGulBaGul.domain.recruitment;

import com.BaGulBaGul.BaGulBaGul.domain.base.BaseTimeEntity;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Getter
@Entity(name="recruitment_comment_child")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentCommentChild extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "recruitment_comment_child_id")
    Long id;

    @JoinColumn(name = "recruitment_comment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    RecruitmentComment recruitmentComment;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    User user;

    @Column(name="content")
    String content;

    @Builder
    public RecruitmentCommentChild(
            RecruitmentComment recruitmentComment,
            User user,
            String content
    ) {
        this.recruitmentComment = recruitmentComment;
        this.user = user;
        this.content = content;
    }
}
