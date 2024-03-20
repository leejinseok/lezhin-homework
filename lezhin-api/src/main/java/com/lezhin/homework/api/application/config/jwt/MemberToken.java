package com.lezhin.homework.api.application.config.jwt;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberToken {

    private Long id;
    private String email;

    public MemberToken(Claims claims) {
        this.id = claims.get("id", Long.class);
        this.email = claims.getSubject();
    }

}
