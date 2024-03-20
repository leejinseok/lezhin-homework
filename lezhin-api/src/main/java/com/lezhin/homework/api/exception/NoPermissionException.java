package com.lezhin.homework.api.exception;

import java.util.function.Supplier;

public class NoPermissionException extends RuntimeException {

    public NoPermissionException(final String message) {
        super(message);
    }

    public static Supplier<NoPermissionException> throwNoPermissionException(final String message) {
        return () -> new NoPermissionException(message);
    }

}
