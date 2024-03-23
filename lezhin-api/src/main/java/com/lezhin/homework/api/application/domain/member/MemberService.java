package com.lezhin.homework.api.application.domain.member;

import com.lezhin.homework.api.application.util.AES256Util;
import com.lezhin.homework.api.exception.NotFoundException;
import com.lezhin.homework.api.presentation.member.dto.MemberRequest;
import com.lezhin.homework.core.db.domain.comic.rate.ComicMemberRate;
import com.lezhin.homework.core.db.domain.comic.rate.ComicMemberRateRepository;
import com.lezhin.homework.core.db.domain.comic.view.ComicViewHistory;
import com.lezhin.homework.core.db.domain.comic.view.ComicViewHistoryRepository;
import com.lezhin.homework.core.db.domain.member.Member;
import com.lezhin.homework.core.db.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.lezhin.homework.api.exception.ExceptionConstants.NOT_FOUND_MEMBER_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ComicMemberRateRepository comicMemberRateRepository;
    private final ComicViewHistoryRepository comicViewHistoryRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public Member save(final MemberRequest memberRequest) {
        String userEmailEncoded = AES256Util.encrypt(memberRequest.getUserEmail());
        String passwordEncoded = passwordEncoder.encode(memberRequest.getPassword());

        Member member = Member.builder()
                .userEmail(userEmailEncoded)
                .userName(memberRequest.getUserName())
                .password(passwordEncoded)
                .gender(memberRequest.getGender())
                .type(memberRequest.getType())
                .build();
        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Page<Member> findAllMemberViewedAdultComicMoreThanThreeTimesAndRegisteredInAWeek(final Pageable pageable) {
        return memberRepository.findAllMemberViewedAdultComicMoreThanThreeTimesAndRegisteredBetween(
                LocalDateTime.now().minusWeeks(1),
                LocalDateTime.now(),
                pageable
        );
    }

    @Transactional
    public void deleteById(final long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER_MESSAGE.formatted(memberId)));
        memberRepository.delete(member);
    }

    @Transactional(readOnly = true)
    public List<Member> findAll() {
        return memberRepository.findAll();
    }
}
