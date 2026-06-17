package com.siju.ecommerce.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super("username already exists: " + username);
    }
}
