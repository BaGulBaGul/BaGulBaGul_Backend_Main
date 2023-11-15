package com.BaGulBaGul.BaGulBaGul.domain.user;

import com.BaGulBaGul.BaGulBaGul.domain.base.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Getter
@Entity(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    Long id;

    @Column(name="email")
    String email;

    @Column(name="nickname")
    String nickname;

    @Column(name="image_uri")
    String imageURI;

    @Builder
    public User(
            String email,
            String nickName,
            String imageURI
    ) {
        this.email = email;
        this.nickname = nickName;
        this.imageURI = imageURI;
    }

}
