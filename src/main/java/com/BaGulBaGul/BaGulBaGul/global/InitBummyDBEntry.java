package com.BaGulBaGul.BaGulBaGul.global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class InitBummyDBEntry {

    @Autowired
    InitDummyDB initDummyDB;

    @PostConstruct
    public void init() {
        initDummyDB.init();
    }
}
