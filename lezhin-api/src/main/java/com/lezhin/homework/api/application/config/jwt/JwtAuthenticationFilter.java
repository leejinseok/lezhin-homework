package com.lezhin.homework.api.application.config.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class JwtAuthenticationFilter implements Filter {

    private final JwtProvider jwtProvider;
    private static final String AUTHORIZATION = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer";

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            String token = getToken(request);
            if (token != null) {
                Authentication authentication = getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException | MalformedJwtException e) {
            PrintWriter writer = httpResponse.getWriter();
            httpResponse.setStatus(401);
            writer.write(e.getMessage());
            writer.flush();
            writer.close();
            return;
        }
        chain.doFilter(request, response);
    }

    private Authentication getAuthentication(String token) {
        Jws<Claims> parse = jwtProvider.parse(token);
        Claims body = parse.getBody();

        if (jwtProvider.checkTokenExpired(body.getExpiration())) {
            throw new ExpiredJwtException(parse.getHeader(), body, "만료 된 토큰입니다. 토큰을 갱신해주세요.");
        }

        Set<GrantedAuthority> roles = new HashSet<>();
        String role = String.valueOf(body.get("role"));
        roles.add(new SimpleGrantedAuthority("ROLE_" + role));
        MemberToken memberTokenMapper = new MemberToken(body);

        return new JwtAuthentication(memberTokenMapper, token, roles);
    }

    private String getToken(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = httpRequest.getHeader(AUTHORIZATION);

        if (token == null || !token.contains(TOKEN_PREFIX)) {
            return null;
        }
        return token.substring("Bearer ".length());
    }


}
