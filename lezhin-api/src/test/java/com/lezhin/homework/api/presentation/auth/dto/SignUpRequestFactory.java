package com.lezhin.homework.api.presentation.auth.dto;

import com.lezhin.homework.core.db.domain.Gender;
import com.lezhin.homework.core.db.domain.member.MemberType;

public class SignUpRequestFactory {

    public static SignUpRequest createSampleSignUpRequest() {
        return SignUpRequest.of(
                "김레진",
                "lezhin@lezhin.com",
                "password",
                Gender.MALE,
                MemberType.NORMAL
        );
    }
}