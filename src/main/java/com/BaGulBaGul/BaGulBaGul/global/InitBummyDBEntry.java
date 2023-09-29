package com.BaGulBaGul.BaGulBaGul.global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class InitBummyDBEntry {

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
