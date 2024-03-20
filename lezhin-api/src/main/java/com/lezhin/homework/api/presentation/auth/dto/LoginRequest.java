package com.lezhin.homework.api.presentation.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class LoginRequest {

    private final String userEmail;
    private final String password;

}
