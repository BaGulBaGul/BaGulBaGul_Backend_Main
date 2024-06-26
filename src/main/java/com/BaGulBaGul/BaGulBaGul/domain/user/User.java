package com.BaGulBaGul.BaGulBaGul.domain.user;

import com.BaGulBaGul.BaGulBaGul.domain.base.BaseTimeEntity;
import javax.persistence.GenerationType;
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
@Entity(name = "user")
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
    @Column(name="deleted")
    boolean deleted;

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
        this.deleted = false;
    }
}
