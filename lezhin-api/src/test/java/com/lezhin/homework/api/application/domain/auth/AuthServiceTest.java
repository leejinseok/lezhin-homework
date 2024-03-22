package com.lezhin.homework.api.application.domain.auth;

import com.lezhin.homework.api.application.config.ApiDbConfig;
import com.lezhin.homework.api.application.config.jwt.JwtConfig;
import com.lezhin.homework.api.application.config.jwt.JwtProvider;
import com.lezhin.homework.api.application.util.AES256Util;
import com.lezhin.homework.api.presentation.auth.dto.LoginRequest;
import com.lezhin.homework.api.presentation.auth.dto.LoginResponse;
import com.lezhin.homework.api.presentation.auth.dto.SignUpRequest;
import com.lezhin.homework.core.db.domain.member.Member;
import com.lezhin.homework.core.db.domain.member.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static com.lezhin.homework.api.presentation.auth.dto.SignUpRequestFactory.createSampleSignUpRequest;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles({"test"})
@DataJpaTest
@Import({ApiDbConfig.class, JwtConfig.class})
class AuthServiceTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    AuthService createAuthService() {
        return new AuthService(jwtProvider, memberRepository, passwordEncoder);
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }


    @DisplayName("회원가입")
    @Test
    void signUp() {
        AuthService authService = createAuthService();

        SignUpRequest signUpRequest = createSampleSignUpRequest();
        Member member = authService.signUp(signUpRequest);

        assertThat(member).isNotNull();
        assertThat(member.getId()).isNotNull();

        String encryptEmail = AES256Util.encrypt(signUpRequest.getUserEmail());
        assertThat(member.getUserEmail()).isEqualTo(encryptEmail);

        boolean passwordMatch = passwordEncoder.matches(signUpRequest.getPassword(), member.getPassword());
        assertThat(passwordMatch).isTrue();

        assertThat(member.getType()).isEqualTo(signUpRequest.getType());
        assertThat(member.getGender()).isEqualTo(signUpRequest.getGender());
        assertThat(member.getUserName()).isEqualTo(signUpRequest.getUserName());
        assertThat(member.getRegisterDateTime()).isNotNull();
    }

    @DisplayName("로그인")
    @Test
    void login() {
        AuthService authService = createAuthService();
        SignUpRequest signUpRequest = createSampleSignUpRequest();
        authService.signUp(signUpRequest);

        LoginResponse login = authService.login(LoginRequest.of(signUpRequest.getUserEmail(), signUpRequest.getPassword()));
        assertThat(login).isNotNull();
        assertThat(login.getMember()).isNotNull();

        Jws<Claims> claims = jwtProvider.parse(login.getToken().getAccessToken());
        assertThat(claims.getBody().getSubject()).isEqualTo(signUpRequest.getUserEmail());
    }

}