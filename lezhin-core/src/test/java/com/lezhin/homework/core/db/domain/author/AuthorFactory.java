package com.lezhin.homework.core.db.domain.author;

public class AuthorFactory {

    public static Author createSampleAuthor() {
        return Author.builder()
                .nickname("김작가")
                .build();
    }
}
