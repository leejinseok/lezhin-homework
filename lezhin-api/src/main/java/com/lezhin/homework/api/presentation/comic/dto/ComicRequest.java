package com.lezhin.homework.api.presentation.comic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ComicRequest {

    @Schema(example = "0")
    private BigDecimal coin;

}
