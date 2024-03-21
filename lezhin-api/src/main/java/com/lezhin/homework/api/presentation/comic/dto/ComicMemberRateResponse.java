package com.lezhin.homework.api.presentation.comic.dto;

import com.lezhin.homework.core.db.domain.comic.rate.ComicMemberRate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class ComicMemberRateResponse {

    private long comicId;
    private long comicMemberRateId;
    private boolean isLike;
    private String comment;
    private LocalDateTime registerDateTime;

    public static ComicMemberRateResponse create(final ComicMemberRate comicMemberRate) {
        return ComicMemberRateResponse.of(
                comicMemberRate.getComic().getId(),
                comicMemberRate.getId(),
                comicMemberRate.getIsLike(),
                comicMemberRate.getComment(),
                comicMemberRate.getRegisterDateTime()
        );
    }
}
