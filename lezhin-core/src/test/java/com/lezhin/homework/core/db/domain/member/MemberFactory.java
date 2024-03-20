package com.lezhin.homework.core.db.domain.member;

import com.lezhin.homework.core.db.domain.Gender;

import java.time.LocalDateTime;

public class MemberFactory {
    public static Member createMember() {
        return Member.builder()
                .userEmail("lezhin@lezhin.com")
                .userName("김레진")
                .type(MemberType.NORMAL)
                .gender(Gender.MALE)
                .registerDate(LocalDateTime.now())
                .build();
    }
}
