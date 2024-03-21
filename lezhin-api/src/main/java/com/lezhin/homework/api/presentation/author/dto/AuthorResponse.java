package com.lezhin.homework.api.presentation.author.dto;

import com.lezhin.homework.core.db.domain.author.Author;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class AuthorResponse {

    private long id;
    private String nickname;

    public static AuthorResponse create(final Author author) {
        return AuthorResponse.of(
                author.getId(), author.getNickname()
        );
    }

}
