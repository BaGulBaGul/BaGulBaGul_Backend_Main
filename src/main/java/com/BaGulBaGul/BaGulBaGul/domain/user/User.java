package com.BaGulBaGul.BaGulBaGul.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    Long id;
}
