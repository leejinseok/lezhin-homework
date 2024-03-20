package com.lezhin.homework.api.application.config.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider(jwtSecretKey);
    }

}
