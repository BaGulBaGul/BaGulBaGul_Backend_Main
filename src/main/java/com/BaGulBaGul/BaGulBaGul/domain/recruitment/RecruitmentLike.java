package com.BaGulBaGul.BaGulBaGul.domain.recruitment;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.RecruitmentLike.RecruitmentLikeId;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "recruitment_like")
@IdClass(RecruitmentLikeId.class)
public class RecruitmentLike {

    @EqualsAndHashCode
    public static class RecruitmentLikeId implements Serializable {
        private Long recruitment;
        private Long user;
    }

    @Id
    @JoinColumn(name = "recruitment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Recruitment recruitment;

    @Id
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public RecruitmentLike(Recruitment recruitment, User user) {
        this.recruitment = recruitment;
        this.user = user;
    }
}
