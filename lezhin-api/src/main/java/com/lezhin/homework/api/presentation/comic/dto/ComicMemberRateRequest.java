package com.lezhin.homework.api.presentation.comic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ComicMemberRateRequest {

    @Schema(example = "1")
    private long comicId;

    @Schema(example = "true")
    private boolean isLike;

    @Schema(example = "재미있는 작품이라고 생각합니다")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]*$", message = "특수 문자는 사용할 수 없습니다.")
    private String comment;

}
