package com.lezhin.homework.api.presentation.comic.dto;

import com.lezhin.homework.core.db.domain.comic.ComicType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ComicRequest {

    @Schema(example = "키다리 아저씨")
    private String contentsName;

    @Schema(example = "1")
    private long authorId;

    @Schema(example = "NORMAL")
    private ComicType type;

    @Schema(example = "0")
    private BigDecimal coin;

    private LocalDate openDate;

    @Schema(example = "0")
    private long likes;

    @Schema(example = "0")
    private long dislikes;

}
