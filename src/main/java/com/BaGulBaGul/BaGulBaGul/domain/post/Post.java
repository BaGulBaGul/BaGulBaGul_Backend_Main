package com.BaGulBaGul.BaGulBaGul.domain.post;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "post")
public class Post {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    Long id;
}
