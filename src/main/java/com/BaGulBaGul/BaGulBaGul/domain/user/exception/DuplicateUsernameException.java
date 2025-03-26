package com.BaGulBaGul.BaGulBaGul.domain.user.exception;

import com.BaGulBaGul.BaGulBaGul.global.exception.utils.ConstraintViolationChecker;
import org.springframework.dao.DataIntegrityViolationException;

public class DuplicateUsernameException extends RuntimeException {
    private static final String uniqueConstraintName = "UK__USER__NICKNAME";

    public static boolean check(DataIntegrityViolationException e) {
        return ConstraintViolationChecker.checkUniqueConstraintViolation(e, uniqueConstraintName);
    }
}
