package com.lezhin.homework.api.application.domain.comic;

import com.lezhin.homework.api.exception.BadRequestException;
import com.lezhin.homework.api.exception.NotFoundException;
import com.lezhin.homework.api.presentation.comic.dto.ComicRequest;
import com.lezhin.homework.core.db.domain.author.Author;
import com.lezhin.homework.core.db.domain.author.AuthorRepository;
import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.comic.ComicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.lezhin.homework.api.exception.ExceptionConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComicService {

    private static final BigDecimal FREE_COIN = new BigDecimal(0);
    private static final BigDecimal MAX_COIN = new BigDecimal(500);
    private static final BigDecimal MIN_COIN = new BigDecimal(100);


    private final ComicRepository comicRepository;
    private final AuthorRepository authorRepository;


    @Transactional(readOnly = true)
    public Comic findById(final long comicId) {
        return comicRepository.findById(comicId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COMIC_MESSAGE.formatted(comicId)));
    }

    @Transactional
    public Comic save(final ComicRequest request) {
        long authorId = request.getAuthorId();
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_AUTHOR_MESSAGE.formatted(authorId)));

        Comic comic = Comic.builder()
                .contentsName(request.getContentsName())
                .coin(request.getCoin())
                .type(request.getType())
                .author(author)
                .openDate(request.getOpenDate())
                .likes(request.getLikes())
                .dislikes(request.getDislikes())
                .build();

        return comicRepository.save(comic);
    }

    @Transactional
    public Comic updateComic(final long comicId, final ComicRequest request) {
        Comic comic = findById(comicId);
        BigDecimal coin = request.getCoin();
        if (coin.compareTo(FREE_COIN) > 0) {
            if (!validPaidCoinRange(coin)) {
                throw new BadRequestException(NOT_VALID_PAID_COIN_RANGE.formatted(coin.toString()));
            }
        }

        comic.updateCoin(request.getCoin());
        return comic;
    }

    public boolean validPaidCoinRange(final BigDecimal coin) {
        int compareToMinCoin = coin.compareTo(MIN_COIN);
        if (compareToMinCoin < 0) {
            return false;
        }
        int compareToMaxCoin = coin.compareTo(MAX_COIN);
        if (compareToMaxCoin > 0) {
            return false;
        }
        return true;
    }


}
