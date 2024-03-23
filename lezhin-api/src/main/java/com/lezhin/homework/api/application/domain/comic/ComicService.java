package com.lezhin.homework.api.application.domain.comic;

import com.lezhin.homework.api.exception.NotFoundException;
import com.lezhin.homework.api.presentation.comic.dto.ComicRequest;
import com.lezhin.homework.core.db.domain.author.Author;
import com.lezhin.homework.core.db.domain.author.AuthorRepository;
import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.comic.ComicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.lezhin.homework.api.exception.ExceptionConstants.NOT_FOUND_AUTHOR_MESSAGE;
import static com.lezhin.homework.api.exception.ExceptionConstants.NOT_FOUND_COMIC_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComicService {

    private final ComicRepository comicRepository;
    private final AuthorRepository authorRepository;

    @Transactional
    public Comic save(final ComicRequest comicRequest) {
        long authorId = comicRequest.getAuthorId();
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_AUTHOR_MESSAGE.formatted(authorId)));

        Comic comic = Comic
                .builder()
                .contentsName(comicRequest.getContentsName())
                .author(author)
                .type(comicRequest.getType())
                .coin(comicRequest.getCoin())
                .likes(comicRequest.getLikes())
                .dislikes(comicRequest.getDislikes())
                .openDate(comicRequest.getOpenDate())
                .build();

        return comicRepository.save(comic);
    }

    @Transactional(readOnly = true)
    public Comic findById(final long comicId) {
        return comicRepository.findById(comicId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COMIC_MESSAGE.formatted(comicId)));
    }

    @Transactional
    public Comic updateComic(final long comicId, final ComicRequest request) {
        Comic comic = findById(comicId);
        comic.updateCoin(request.getCoin());
        return comic;
    }


}
