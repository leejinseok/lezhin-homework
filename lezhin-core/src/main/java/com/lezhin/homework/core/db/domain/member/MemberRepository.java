package com.lezhin.homework.core.db.domain.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserEmail(String email);

    @Query("""
            select member from Member member
            where (
                select count(comicViewHisotry) from ComicViewHistory comicViewHisotry
                where
                    comicViewHisotry.member = member
            ) >= 3 and member.registerDateTime between :startDate and :endDate
            """)
    Page<Member> findAllMemberViewedAdultComicMoreThanThreeTimesAndRegisteredBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
