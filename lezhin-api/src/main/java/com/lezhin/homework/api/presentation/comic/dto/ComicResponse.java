package com.lezhin.homework.api.presentation.comic.dto;

import com.lezhin.homework.api.presentation.author.dto.AuthorResponse;
import com.lezhin.homework.core.db.domain.comic.Comic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class ComicResponse {

    @Schema(example = "1")
    private long id;

    @Schema(example = "키다리 아저씨")
    private String contentsName;

    private AuthorResponse author;

    @Schema(example = "0")
    private BigDecimal coin;

    private LocalDate openDate;

    @Schema(example = "10")
    private long likes;

    @Schema(example = "5")
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
