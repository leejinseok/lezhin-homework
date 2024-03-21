package com.lezhin.homework.api.application.domain.member;

import com.lezhin.homework.api.application.util.AES256Util;
import com.lezhin.homework.core.db.domain.Gender;
import com.lezhin.homework.core.db.domain.member.Member;
import com.lezhin.homework.core.db.domain.member.MemberType;

public class MemberFactory {

    public static Member createSampleMember() {
        return Member.builder()
                .id(1L)
                .userName("김레진")
                .gender(Gender.MALE)
                .type(MemberType.NORMAL)
                .password("password")
                .userEmail(AES256Util.encrypt("lezhin@lezhin.com"))
                .build();
    }
}
