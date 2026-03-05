package com.BaGulBaGul.BaGulBaGul.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class AdminManagePasswordLoginUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_manage_password_login_user_id")
    Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "login_id")
    PasswordLoginUser passwordLoginUser;

    @Builder
    public AdminManagePasswordLoginUser(PasswordLoginUser passwordLoginUser) {
        this.passwordLoginUser = passwordLoginUser;
    }
}
