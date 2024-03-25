package com.lezhin.homework.api.application.domain.auth;

import com.lezhin.homework.api.application.config.jwt.JwtProvider;
import com.lezhin.homework.api.application.util.AES256Util;
import com.lezhin.homework.api.exception.NotFoundException;
import com.lezhin.homework.api.exception.UnAuthorizedException;
import com.lezhin.homework.api.presentation.auth.dto.LoginRequest;
import com.lezhin.homework.api.presentation.auth.dto.LoginResponse;
import com.lezhin.homework.api.presentation.auth.dto.SignUpRequest;
import com.lezhin.homework.api.presentation.auth.dto.TokenResponse;
import com.lezhin.homework.api.presentation.member.dto.MemberResponse;
import com.lezhin.homework.core.db.domain.Gender;
import com.lezhin.homework.core.db.domain.member.Member;
import com.lezhin.homework.core.db.domain.member.MemberRepository;
import com.lezhin.homework.core.db.domain.member.MemberType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member signUp(final SignUpRequest signUpRequest) {

        String userName = signUpRequest.getUserName();
        String userEmail = signUpRequest.getUserEmail();
        String password = signUpRequest.getPassword();
        Gender gender = signUpRequest.getGender();
        MemberType type = signUpRequest.getType();

        String emailEncrypt = AES256Util.encrypt(userEmail);
        String passwordEncoded = passwordEncoder.encode(password);

        Member member = Member.create(
                userName, emailEncrypt, passwordEncoded, gender, type, LocalDateTime.now()
        );

        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(final LoginRequest loginRequest) {
        String email = loginRequest.getUserEmail();
        String decrypt = AES256Util.encrypt(email);
        Member member = memberRepository.findByUserEmail(decrypt).orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));

        String password = member.getPassword();
        if (!passwordEncoder.matches(loginRequest.getPassword(), password)) {
            throw new UnAuthorizedException("패스워드가 일치하지 않습니다.");
        }

        String accessToken = jwtProvider.createToken(member);
        String refreshToken = jwtProvider.createRefreshToken(member);

        MemberResponse memberResponse = MemberResponse.of(
                member.getId(), member.getUserName(), email, member.getGender(), member.getMemberType(), member.getRegisterDateTime()
        );
        TokenResponse tokenResponse = TokenResponse.of(accessToken, refreshToken);

        return LoginResponse.of(memberResponse, tokenResponse);
    }

}
