package com.BaGulBaGul.BaGulBaGul.domain.post;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id
    @GeneratedValue
    @Column(name = "category_id")
    Long id;

    @Setter
    @Column(name = "name")
    String name;

    @Builder
    public Category(
            String name
    ) {
        this.name = name;
    }
}
