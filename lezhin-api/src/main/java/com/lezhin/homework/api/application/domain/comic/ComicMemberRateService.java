package com.lezhin.homework.api.application.domain.comic;

import com.lezhin.homework.api.exception.NotFoundException;
import com.lezhin.homework.api.presentation.comic.dto.ComicMemberRateRequest;
import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.comic.ComicRepository;
import com.lezhin.homework.core.db.domain.comic.rate.ComicMemberRate;
import com.lezhin.homework.core.db.domain.comic.rate.ComicMemberRateRepository;
import com.lezhin.homework.core.db.domain.member.Member;
import com.lezhin.homework.core.db.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.lezhin.homework.api.exception.ExceptionConstants.NOT_FOUND_COMIC_MESSAGE;
import static com.lezhin.homework.api.exception.ExceptionConstants.NOT_FOUND_MEMBER_MESSAGE;

@Service
@RequiredArgsConstructor
public class ComicMemberRateService {

    private final ComicRepository comicRepository;
    private final ComicMemberRateRepository comicMemberRateRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ComicMemberRate rateComic(final long memberId, final ComicMemberRateRequest request) {
        long comicId = request.getComicId();
        Comic comic = comicRepository.findById(comicId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COMIC_MESSAGE.formatted(comicId)));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER_MESSAGE.formatted(memberId)));

        ComicMemberRate comicMemberRate = ComicMemberRate.create(
                comic, member, request.isLike(), request.getComment()
        );

        return comicMemberRateRepository.save(comicMemberRate);
    }

    @Transactional
    public Comic countComicLikeAndDislikeCount(final long comicId) {
        Comic comic = comicRepository.findById(comicId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COMIC_MESSAGE.formatted(comicId)));

        List<ComicMemberRate> rates = comicMemberRateRepository.findAllByComicId(comicId);
        int likes = 0;
        int dislikes = 0;
        for (ComicMemberRate rate : rates) {
            if (rate.getIsLike()) {
                likes++;
            } else {
                dislikes++;
            }
        }
        comic.updateLikesAndDislikes(likes, dislikes);
        return comic;
    }

}
