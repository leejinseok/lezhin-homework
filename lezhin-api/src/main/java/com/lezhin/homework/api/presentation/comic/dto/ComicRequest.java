package com.lezhin.homework.api.presentation.comic.dto;

import com.lezhin.homework.core.db.domain.comic.ComicType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ComicRequest {

    private String contentsName;
    private long authorId;
    private BigDecimal coin;
    private ComicType type;
    private long likes;
    private long dislikes;
    private LocalDate openDate;

    public static ComicRequest of(final BigDecimal coin) {
        ComicRequest comicRequest = new ComicRequest();
        comicRequest.coin = coin;
        return comicRequest;
    }

}
