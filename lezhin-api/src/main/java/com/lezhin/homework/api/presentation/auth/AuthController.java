package com.lezhin.homework.api.presentation.auth;

import com.lezhin.homework.api.application.domain.auth.AuthService;
import com.lezhin.homework.api.presentation.auth.dto.LoginRequest;
import com.lezhin.homework.api.presentation.auth.dto.LoginResponse;
import com.lezhin.homework.api.presentation.auth.dto.SignUpRequest;
import com.lezhin.homework.api.presentation.member.dto.MemberResponse;
import com.lezhin.homework.core.db.domain.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(description = "회원가입", summary = "회원가입")
    @PostMapping("/sign-up")
    public ResponseEntity<MemberResponse> signUp(@RequestBody final SignUpRequest signUpRequest) {
        Member member = authService.signUp(signUpRequest);

        MemberResponse response = MemberResponse.of(
                member.getId(),
                member.getUserName(),
                signUpRequest.getUserEmail(),
                member.getGender(),
                member.getType(),
                member.getRegisterDateTime()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(description = "로그인", summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody final LoginRequest loginRequest) {
        LoginResponse login = authService.login(loginRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(login);
    }

}
