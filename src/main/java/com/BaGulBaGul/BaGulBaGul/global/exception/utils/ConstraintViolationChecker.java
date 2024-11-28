package com.BaGulBaGul.BaGulBaGul.global.exception.utils;

import org.springframework.dao.DataIntegrityViolationException;

public abstract class ConstraintViolationChecker {
    public static boolean checkUniqueConstraintViolation(DataIntegrityViolationException e, String constraintName) {
        if(e.getMessage().contains(constraintName)) {
            return true;
        }
        return false;
    }
}
