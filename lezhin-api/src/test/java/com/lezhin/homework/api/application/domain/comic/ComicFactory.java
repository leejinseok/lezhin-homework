package com.lezhin.homework.api.application.domain.comic;

import com.lezhin.homework.core.db.domain.author.Author;
import com.lezhin.homework.core.db.domain.comic.Comic;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ComicFactory {

    public static Comic createSampleComic(final Long id, final Author author, final BigDecimal coin) {
        return Comic
                .builder()
                .id(id)
                .author(author)
                .contentsName("키다리 아저씨")
                .openDate(LocalDate.now())
                .likes(0L)
                .dislikes(0L)
                .coin(coin)
                .build();
    }

}
