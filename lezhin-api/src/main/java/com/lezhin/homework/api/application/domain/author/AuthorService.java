package com.lezhin.homework.api.application.domain.author;

import com.lezhin.homework.api.presentation.author.dto.AuthorRequest;
import com.lezhin.homework.core.db.domain.author.Author;
import com.lezhin.homework.core.db.domain.author.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Transactional
    public Author save(final AuthorRequest request) {
        Author author = Author.builder()
                .nickname(request.getNickname())
                .build();
        return authorRepository.save(author);
    }

}
