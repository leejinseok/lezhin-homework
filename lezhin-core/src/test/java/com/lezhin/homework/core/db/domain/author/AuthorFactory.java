package com.lezhin.homework.core.db.domain.author;

public class AuthorFactory {

    public static Author createAuthor() {
        return Author.builder()
                .nickname("김작가")
                .build();
    }
}
