package com.lezhin.homework.api.application.domain.comic;

import com.lezhin.homework.api.exception.NotFoundException;
import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.comic.search.ComicViewHistory;
import com.lezhin.homework.core.db.domain.comic.search.ComicViewHistoryRepository;
import com.lezhin.homework.core.db.domain.member.Member;
import com.lezhin.homework.core.db.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.lezhin.homework.api.exception.ExceptionConstants.NOT_FOUND_MEMBER_MESSAGE;

@Service
@RequiredArgsConstructor
public class ComicViewHistoryService {

    private final ComicViewHistoryRepository comicViewHistoryRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Page<ComicViewHistory> findAllByComicId(final long comicId, final Pageable pageable) {
        return comicViewHistoryRepository.findAllByComicId(comicId, pageable);
    }

    @Transactional
    public void saveViewHistory(final Comic comic, final long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER_MESSAGE.formatted(memberId)));

        ComicViewHistory viewHistory = ComicViewHistory.create(
                member,
                comic,
                LocalDateTime.now()
        );

        comicViewHistoryRepository.save(viewHistory);
    }

}
