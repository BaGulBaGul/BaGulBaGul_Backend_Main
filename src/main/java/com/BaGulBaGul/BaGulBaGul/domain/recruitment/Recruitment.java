package com.BaGulBaGul.BaGulBaGul.domain.recruitment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "recruitment")
public class Recruitment {
    @Id
    @GeneratedValue
    @Column(name = "recruitment_id")
    Long id;
}
