package com.lezhin.homework.api.presentation.member.dto;

import com.lezhin.homework.core.db.domain.Gender;
import com.lezhin.homework.core.db.domain.member.MemberType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class MemberResponse {

    private Long id;
    private String userName;
    private String userEmail;
    private Gender gender;
    private MemberType type;
    private LocalDateTime registerDateTime;


}
