package com.lezhin.homework.api.presentation.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lezhin.homework.api.application.config.ApiSecurityConfig;
import com.lezhin.homework.api.application.config.jwt.JwtConfig;
import com.lezhin.homework.api.application.config.jwt.JwtProvider;
import com.lezhin.homework.api.application.domain.auth.AuthService;
import com.lezhin.homework.api.application.util.AES256Util;
import com.lezhin.homework.api.presentation.auth.dto.LoginRequest;
import com.lezhin.homework.api.presentation.auth.dto.LoginResponse;
import com.lezhin.homework.api.presentation.auth.dto.SignUpRequest;
import com.lezhin.homework.api.presentation.auth.dto.TokenResponse;
import com.lezhin.homework.api.presentation.member.dto.MemberResponse;
import com.lezhin.homework.core.db.domain.Gender;
import com.lezhin.homework.core.db.domain.member.Member;
import com.lezhin.homework.core.db.domain.member.MemberType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static com.lezhin.homework.api.presentation.auth.dto.SignUpRequestFactory.createSampleSignUpRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = {AuthController.class})
@Import({ApiSecurityConfig.class, JwtConfig.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @MockBean
    private AuthService authService;

    @DisplayName("회원가입")
    @Test
    void signUp() throws Exception {
        SignUpRequest request = createSampleSignUpRequest();
        byte[] content = objectMapper.writeValueAsBytes(request);

        Member saved = Member.builder()
                .id(1L)
                .userName(request.getUserName())
                .userEmail(AES256Util.encrypt(request.getUserEmail()))
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        when(authService.signUp(any())).thenReturn(saved);

        mockMvc.perform(
                        post("/api/v1/auth/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andDo(print())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.userName").value(saved.getUserName()))
                .andExpect(jsonPath("$.userEmail").value(AES256Util.decrypt(saved.getUserEmail())));
    }

    @DisplayName("로그인")
    @Test
    void login() throws Exception {
        LoginRequest request = LoginRequest.of("lezhin@lezhin.com", "password");
        byte[] content = objectMapper.writeValueAsBytes(request);

        Member member = Member.builder()
                .id(1L)
                .userName("김레진")
                .userEmail(AES256Util.encrypt(request.getUserEmail()))
                .password(passwordEncoder.encode(request.getPassword()))
                .registerDateTime(LocalDateTime.now())
                .memberType(MemberType.NORMAL)
                .gender(Gender.MALE)
                .build();

        MemberResponse memberResponse = MemberResponse.create(member);

        String accessToken = jwtProvider.createToken(member);
        String refreshToken = jwtProvider.createRefreshToken(member);
        TokenResponse tokenResponse = TokenResponse.of(accessToken, refreshToken);
        LoginResponse loginResponse = LoginResponse.of(memberResponse, tokenResponse);

        when(authService.login(any())).thenReturn(loginResponse);

        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andDo(print())
                .andExpect(jsonPath("$.member").exists())
                .andExpect(jsonPath("$.member.id").value(member.getId()))
                .andExpect(jsonPath("$.member.userName").value(member.getUserName()))
                .andExpect(jsonPath("$.member.userEmail").value(AES256Util.decrypt(member.getUserEmail())))
                .andExpect(jsonPath("$.member.gender").value(member.getGender().name()))
                .andExpect(jsonPath("$.member.type").value(member.getMemberType().name()))
                .andExpect(jsonPath("$.token.accessToken").value(accessToken))
                .andExpect(jsonPath("$.token.refreshToken").value(refreshToken));
    }


}