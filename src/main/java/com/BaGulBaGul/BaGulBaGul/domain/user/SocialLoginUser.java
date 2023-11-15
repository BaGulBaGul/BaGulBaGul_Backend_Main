package com.BaGulBaGul.BaGulBaGul.domain.user;

import com.BaGulBaGul.BaGulBaGul.domain.user.auth.oauth2.constant.OAuth2Provider;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialLoginUser {
    @Id
    @Column(name = "social_login_user_id")
    String id;

    @Column(name = "provider")
    @Enumerated(value = EnumType.STRING)
    OAuth2Provider provider;

    @JoinColumn(name = "user_id")
    @OneToOne
    User user;

    @Builder
    public SocialLoginUser(
            String id,
            OAuth2Provider provider,
            User user
            ) {
        this.id = id;
        this.provider = provider;
        this.user = user;
    }
}
