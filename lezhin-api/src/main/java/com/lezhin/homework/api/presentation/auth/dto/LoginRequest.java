package com.lezhin.homework.api.presentation.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class LoginRequest {

    @Schema(example = "lezhin@lezhin.com")
    private final String userEmail;

    @Schema(example = "password")
    private final String password;

}
