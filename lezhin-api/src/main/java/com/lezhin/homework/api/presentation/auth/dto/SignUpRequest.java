package com.lezhin.homework.api.presentation.auth.dto;

import com.lezhin.homework.core.db.domain.Gender;
import com.lezhin.homework.core.db.domain.member.MemberType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class SignUpRequest {

    private String userName;
    private String userEmail;
    private String password;
    private Gender gender;
    private MemberType type;


}
