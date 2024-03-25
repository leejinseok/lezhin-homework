package com.lezhin.homework.api.presentation.member.dto;

import com.lezhin.homework.api.application.util.AES256Util;
import com.lezhin.homework.core.db.domain.Gender;
import com.lezhin.homework.core.db.domain.member.Member;
import com.lezhin.homework.core.db.domain.member.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class MemberResponse {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "김레진")
    private String userName;

    @Schema(example = "lezhin@lezhin.com")
    private String userEmail;

    @Schema(example = "MALE")
    private Gender gender;

    @Schema(example = "NORMAL")
    private MemberType type;

    private LocalDateTime registerDateTime;

    public static MemberResponse create(final Member member) {
        return MemberResponse.of(
                member.getId(),
                member.getUserName(),
                AES256Util.decrypt(member.getUserEmail()),
                member.getGender(),
                member.getMemberType(),
                member.getRegisterDateTime()
        );
    }

}
