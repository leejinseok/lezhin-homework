package com.lezhin.homework.api.presentation.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {

    private String message;

    public static ErrorResponse of(final String message) {
        ErrorResponse response = new ErrorResponse();
        response.message = message;
        return response;
    }

}
