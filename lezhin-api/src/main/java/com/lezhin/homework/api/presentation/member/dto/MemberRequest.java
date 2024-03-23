package com.lezhin.homework.api.presentation.member.dto;

import com.lezhin.homework.core.db.domain.Gender;
import com.lezhin.homework.core.db.domain.member.MemberType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class MemberRequest {

    private String userName;
    private String userEmail;
    private String password;
    private MemberType type;
    private Gender gender;


}
