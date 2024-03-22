package com.lezhin.homework.core.db.domain.comic.view;

import com.lezhin.homework.core.db.domain.member.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.lezhin.homework.core.db.domain.comic.view.QComicViewHistory.comicViewHistory;
import static com.lezhin.homework.core.db.domain.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class ComicViewHistoryQueryDslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Page<Member> findAllMemberAdultComicVisitDateTimeBetween(
            final LocalDateTime startDateTime,
            final LocalDateTime endDateTime,
            final Pageable pageable
    ) {
        List<Member> fetch = jpaQueryFactory.select(comicViewHistory.member)
                .from(comicViewHistory)
                .join(comicViewHistory.member, member)
                .distinct()
                .where(comicViewHistory.viewDateTime.between(startDateTime, endDateTime))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long count = jpaQueryFactory
                .select(comicViewHistory.member.countDistinct())
                .from(comicViewHistory)
                .join(comicViewHistory.member, member)
                .distinct()
                .where(comicViewHistory.viewDateTime.between(startDateTime, endDateTime))
                .fetchOne();

        return new PageImpl<>(fetch, pageable, count);
    }


}
