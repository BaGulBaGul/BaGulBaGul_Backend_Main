package com.BaGulBaGul.BaGulBaGul.domain.user;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.global.upload.Resource;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserImage {
    @Id
    @Column(name = "resource_id")
    Long resourceId;

    @MapsId
    @JoinColumn(name = "resource_id")
    @OneToOne(fetch = FetchType.LAZY)
    Resource resource;

    @JoinColumn(name = "user_id")
    @OneToOne(fetch = FetchType.LAZY)
    User user;

    @Builder
    public UserImage(User user, Resource resource) {
        this.user = user;
        this.resource = resource;
    }
}
