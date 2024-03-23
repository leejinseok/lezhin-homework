package com.lezhin.homework.api.presentation.author.dto;

import com.lezhin.homework.core.db.domain.author.Author;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class AuthorRequest {

    @Schema(example = "1")
    private long id;

    @Schema(example = "김작가")
    private String nickname;

    public static AuthorRequest create(final Author author) {
        return AuthorRequest.of(
                author.getId(), author.getNickname()
        );
    }

}
