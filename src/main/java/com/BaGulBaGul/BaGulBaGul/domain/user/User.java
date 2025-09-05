package com.BaGulBaGul.BaGulBaGul.domain.user;

import com.BaGulBaGul.BaGulBaGul.domain.base.BaseTimeEntity;
import com.BaGulBaGul.BaGulBaGul.global.auth.Role;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.FetchType;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Setter;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Long id;

    @Setter
    @Column(name="email")
    String email;

    @Setter
    @Column(name="nickname", unique = true)
    String nickname;

    @Setter
    @Column(name="profile_message")
    String profileMessage;

    @Setter
    @Column(name="image_uri")
    String imageURI;

    @Setter
    @Column(name = "is_suspended", nullable = false)
    private boolean isSuspended = false;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    Set<UserRole> userRoles = new HashSet<>();

    @Setter
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    SocialLoginUser socialLoginUser;

    @Setter
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    PasswordLoginUser passwordLoginUser;

    @Builder
    public User(
            String email,
            String nickName,
            String profileMessage,
            String imageURI
    ) {
        this.email = email;
        this.nickname = nickName;
        this.profileMessage = profileMessage;
        this.imageURI = imageURI;
    }

    public Set<Role> getRoles() {
        return userRoles.stream().map(UserRole::getRole).collect(Collectors.toSet());
    }

    /**
     * 유저 정지 여부에 대한 플래그.
     * 정지 만료일이 지났는지 확인해서 업데이트해야 하므로 이 플래그를 외부에서 직접 사용하지 마세요.
     * 대신 UserSuspensionService를 이용하세요
     */
    public boolean isSuspended() {
        return isSuspended;
    }
}
