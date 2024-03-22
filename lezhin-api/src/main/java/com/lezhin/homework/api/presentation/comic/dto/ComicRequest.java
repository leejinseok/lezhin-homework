package com.lezhin.homework.api.presentation.comic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ComicRequest {

    private BigDecimal coin;

}
