package com.lezhin.homework.core.db.domain.comic;

import com.lezhin.homework.core.db.domain.author.Author;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ComicFactory {

    public static Comic createSampleComic(final Author author, final BigDecimal coin) {
        return Comic
                .builder()
                .author(author)
                .contentsName("키다리 아저씨")
                .openDate(LocalDate.now())
                .likes(0L)
                .dislikes(0L)
                .coin(coin)
                .build();

    }
}
