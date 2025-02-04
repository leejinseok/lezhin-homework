package com.lezhin.homework.api.application.domain.member;

import com.lezhin.homework.api.exception.NotFoundException;
import com.lezhin.homework.core.db.domain.member.Member;
import com.lezhin.homework.core.db.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.lezhin.homework.api.exception.ExceptionConstants.NOT_FOUND_MEMBER_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

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

}
