package com.BaGulBaGul.BaGulBaGul.domain.post;

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
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage {
    @Id
    @Column(name = "resource_id")
    Long resourceId;

    @MapsId
    @JoinColumn(name = "resource_id")
    @OneToOne(fetch = FetchType.LAZY)
    Resource resource;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Post post;

    @Setter
    @Column(name = "image_order")
    int order;

    @Builder
    public PostImage(Post post, Resource resource, int order) {
        this.post = post;
        this.resource = resource;
        this.order = order;
    }
}
