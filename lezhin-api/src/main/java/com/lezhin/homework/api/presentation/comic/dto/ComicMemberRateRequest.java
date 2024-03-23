package com.lezhin.homework.api.presentation.comic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ComicMemberRateRequest {

    @Schema(example = "1")
    private long comicId;

    @Schema(example = "true")
    private boolean isLike;

    @Schema(example = "재미있는 작품이라고 생각합니다")
    private String comment;

}
