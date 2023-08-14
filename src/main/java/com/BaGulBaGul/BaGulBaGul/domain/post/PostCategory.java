package com.BaGulBaGul.BaGulBaGul.domain.post;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Entity
@IdClass(PostCategory.PostCategoryId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCategory {
    @EqualsAndHashCode
    public static class PostCategoryId implements Serializable {
        Long post;
        Long category;
    }

    @Id
    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Post post;

    @Id
    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Category category;

    public PostCategory(
            Post post,
            Category category
    ) {
        this.post = post;
        this.category = category;
    }
}
