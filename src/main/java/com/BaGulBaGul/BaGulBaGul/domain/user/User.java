package com.BaGulBaGul.BaGulBaGul.domain.user;

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
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    Long id;

    @Column(name="email")
    String email;

    @Column(name="nickname")
    String nickname;

    @Column(name="sex")
    String sex;

    @Column(name="image_url")
    String imageURL;

    @Builder
    public User(
            String email,
            String nickName,
            String sex,
            String imageURL
    ) {
        this.email = email;
        this.nickname = nickName;
        this.sex = sex;
        this.imageURL = imageURL;
    }

}
