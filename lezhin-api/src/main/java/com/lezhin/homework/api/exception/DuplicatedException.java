package com.lezhin.homework.api.exception;

public class DuplicatedException extends RuntimeException {

    public DuplicatedException(final String message) {
        super(message);
    }

}