package com.lezhin.homework.api.presentation.auth.dto;

import com.lezhin.homework.api.presentation.member.dto.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class LoginResponse {

    private MemberResponse member;
    private TokenResponse token;

}
