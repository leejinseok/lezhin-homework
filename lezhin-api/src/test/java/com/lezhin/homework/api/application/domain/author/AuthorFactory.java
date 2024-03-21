package com.lezhin.homework.api.application.domain.author;

import com.lezhin.homework.core.db.domain.author.Author;

public class AuthorFactory {

    public static Author createSampleAuthor(final Long id) {
        return Author.builder()
                .id(id)
                .nickname("김작가")
                .build();
    }

}
