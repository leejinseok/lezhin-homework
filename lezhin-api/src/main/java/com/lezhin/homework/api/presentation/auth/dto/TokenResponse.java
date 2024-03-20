package com.lezhin.homework.api.presentation.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class TokenResponse {

    private String accessToken;
    private String refreshToken;


}
