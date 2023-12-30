package com.BaGulBaGul.BaGulBaGul.global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Profile("db-dev")
public class InitDummyDBEntry {

    @Autowired
    InitDummyDB initDummyDB;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    String DDL_AUTO;

    @PostConstruct
    public void init() {
        if(DDL_AUTO.startsWith("create")) {
            initDummyDB.init();
        }
    }
}
