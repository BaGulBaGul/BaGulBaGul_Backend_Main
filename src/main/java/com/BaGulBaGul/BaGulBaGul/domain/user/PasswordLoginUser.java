package com.BaGulBaGul.BaGulBaGul.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

@Entity
@Getter
@NoArgsConstructor
public class PasswordLoginUser {
    @Id
    @Column(name = "login_id")
    String loginId;

    @Setter
    @Column(name = "encoded_login_password")
    String encodedLoginPassword;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    User user;

    @Setter
    @OneToOne(mappedBy = "passwordLoginUser", fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    AdminManagePasswordLoginUser adminManagePasswordLoginUser;

    @Builder
    public PasswordLoginUser(
            User user,
            String loginId,
            String encodedLoginPassword
    ) {
        this.user = user;
        this.loginId = loginId;
        this.encodedLoginPassword = encodedLoginPassword;
    }
}
