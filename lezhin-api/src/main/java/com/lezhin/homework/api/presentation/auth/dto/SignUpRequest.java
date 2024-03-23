package com.lezhin.homework.api.presentation.auth.dto;

import com.lezhin.homework.core.db.domain.Gender;
import com.lezhin.homework.core.db.domain.member.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class SignUpRequest {

    @Schema(example = "김레진")
    private String userName;

    @Schema(example = "lezhin@lezhin.com")
    private String userEmail;

    @Schema(example = "password")
    private String password;

    @Schema(example = "MALE")
    private Gender gender;

    @Schema(example = "NORMAL")
    private MemberType type;


}
