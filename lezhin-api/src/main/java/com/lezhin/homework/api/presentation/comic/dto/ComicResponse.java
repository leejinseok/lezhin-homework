package com.lezhin.homework.api.presentation.comic.dto;

import com.lezhin.homework.api.presentation.author.dto.AuthorResponse;
import com.lezhin.homework.core.db.domain.comic.Comic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class ComicResponse {

    private long id;
    private String contentsName;
    private AuthorResponse author;
    private BigDecimal coin;
    private LocalDate openDate;
    private long likes;
    private long dislikes;

    public static ComicResponse create(final Comic comic) {
        return ComicResponse.of(
                comic.getId(),
                comic.getContentsName(),
                AuthorResponse.create(comic.getAuthor()),
                comic.getCoin(),
                comic.getOpenDate(),
                comic.getLikes(),
                comic.getDislikes()
        );
    }
}
