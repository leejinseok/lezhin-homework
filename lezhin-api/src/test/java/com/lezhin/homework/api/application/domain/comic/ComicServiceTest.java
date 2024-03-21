package com.lezhin.homework.api.application.domain.comic;

import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.comic.ComicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
class ComicServiceTest {

    @InjectMocks
    private ComicService comicService;

    @Mock
    private ComicRepository comicRepository;

    @Test
    void comicsTopLikesThree() {
        when(comicRepository.findTopLikesThree()).thenReturn(
                List.of(
                        new Comic(),
                        new Comic(),
                        new Comic()
                )
        );


    }

}