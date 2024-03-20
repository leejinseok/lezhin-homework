package com.lezhin.homework.api.presentation.common.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationErrorResponse {

    private final List<String> errors = new ArrayList<>();

}
