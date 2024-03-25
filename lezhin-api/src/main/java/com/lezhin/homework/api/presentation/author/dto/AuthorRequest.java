package com.lezhin.homework.api.presentation.author.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class AuthorRequest {

    @Schema(example = "김작가")
    private String nickname;

}
