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

    @Value("${FLAG_INIT_DUMMY_DATA}")
    boolean FLAG_INIT_DUMMY_DB;

    @PostConstruct
    public void init() {
        if(FLAG_INIT_DUMMY_DB) {
            initDummyDB.init();
        }
    }
}
