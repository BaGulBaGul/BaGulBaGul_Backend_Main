package com.BaGulBaGul.BaGulBaGul.domain.post;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostS3Image {
    @Id
    @Column(name = "s3_key")
    String key;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Post post;

    @Setter
    @Column(name = "image_order")
    int order;

    @Builder
    public PostS3Image(Post post, String key, int order) {
        this.post = post;
        this.key = key;
        this.order = order;
    }
}
