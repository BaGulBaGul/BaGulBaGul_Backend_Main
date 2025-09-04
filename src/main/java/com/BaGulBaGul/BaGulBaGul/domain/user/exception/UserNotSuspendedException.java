package com.BaGulBaGul.BaGulBaGul.domain.user.exception;

public class UserNotSuspendedException extends RuntimeException {
    public UserNotSuspendedException() {
        super("User is not suspended.");
    }
}
