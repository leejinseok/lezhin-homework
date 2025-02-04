package com.lezhin.homework.core.db.domain.member;

import com.lezhin.homework.core.db.domain.Gender;

import java.time.LocalDateTime;

public class MemberFactory {
    public static Member createSampleMember() {
        return Member.builder()
                .userEmail("lezhin@lezhin.com")
                .userName("김레진")
                .password("password")
                .memberType(MemberType.NORMAL)
                .gender(Gender.MALE)
                .registerDateTime(LocalDateTime.now())
                .build();
    }
}
