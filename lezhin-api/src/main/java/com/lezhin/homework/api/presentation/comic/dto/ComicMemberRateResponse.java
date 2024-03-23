package com.lezhin.homework.api.presentation.comic.dto;

import com.lezhin.homework.core.db.domain.comic.rate.ComicMemberRate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class ComicMemberRateResponse {

    @Schema(example = "1")
    private long comicId;

    @Schema(example = "1")
    private long comicMemberRateId;

    @Schema(example = "true")
    private boolean isLike;

    @Schema(example = "재미있는 작품이라고 생각합니다")
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
