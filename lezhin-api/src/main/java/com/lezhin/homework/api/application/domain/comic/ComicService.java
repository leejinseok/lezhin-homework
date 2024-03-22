package com.lezhin.homework.api.application.domain.comic;

import com.lezhin.homework.api.exception.NotFoundException;
import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.comic.ComicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.lezhin.homework.api.exception.ExceptionConstants.NOT_FOUND_COMIC_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComicService {

    private final ComicRepository comicRepository;

    @Transactional(readOnly = true)
    public Comic findById(final long comicId) {
        return comicRepository.findById(comicId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COMIC_MESSAGE.formatted(comicId)));
    }

    @Transactional
    public Comic updateComicCoin(final long comicId, final BigDecimal coin) {
        Comic comic = findById(comicId);
        comic.updateCoin(coin);
        return comic;
    }


}
