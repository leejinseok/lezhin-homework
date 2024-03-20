package com.lezhin.homework.api.application.config.jwt;


import com.lezhin.homework.api.application.util.AES256Util;
import com.lezhin.homework.core.db.domain.member.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtProvider {

    private final Key key;

    private static Long ACCESS_TOKEN_EXPIRE = (long) 1000 * 60 * 30; // 30분
    private static Long REFRESH_TOKEN_EXPIRE = (long) 1000 * 60 * 60 * 24 * 15; // 15일

    public JwtProvider(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createToken(Member member) {
        Claims claims = getClaimsFrom(member);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Member member) {
        Claims claims = getClaimsFrom(member);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims getClaimsFrom(Member member) {
        Claims claims = Jwts.claims().setSubject(AES256Util.decrypt(member.getUserEmail()));
        claims.put("id", member.getId());
        claims.put("role", "MEMBER");
        return claims;
    }

    public boolean checkTokenExpired(Date expiration) {
        return expiration.getTime() <= new Date().getTime();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

}
