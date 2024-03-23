package com.lezhin.homework.api.application.domain.comic;

import com.lezhin.homework.api.application.config.ApiCacheConfig;
import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.comic.ComicRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApiCacheConfig.class, ComicCacheService.class})
class ComicCacheServiceTest {

    @Autowired
    private ComicCacheService comicCacheService;

    @MockBean
    private ComicRepository comicRepository;

    @DisplayName("좋아요 많은순으로 웹툰 3개를 조회할때 캐싱이 제대로 이루어 지는가?")
    @Test
    void comicsTopLikesThree() {
        when(comicRepository.findTopLikesThree()).thenReturn(
                List.of(
                        new Comic(),
                        new Comic(),
                        new Comic()
                )
        );

        // 캐싱이 이루어지기 때문에 첫번째 호출 이후에는 캐싱에서 조회가 되어야 함
        comicCacheService.comicsTopLikesThree();
        comicCacheService.comicsTopLikesThree();
        comicCacheService.comicsTopLikesThree();

        // 그러므로 실제 DB 조회는 한번만 일어나야한다
        verify(comicRepository, times(1)).findTopLikesThree();
    }

    @DisplayName("싫어요 많은순으로 웹툰 3개를 조회할때 캐싱이 제대로 이루어 지는가?")
    @Test
    void comicsTopDislikesThree() {
        when(comicRepository.findTopDislikesThree()).thenReturn(
                List.of(
                        new Comic(),
                        new Comic(),
                        new Comic()
                )
        );

        // 캐싱이 이루어지기 때문에 첫번째 호출 이후에는 캐싱에서 조회가 되어야 함
        comicCacheService.comicsTopDislikesThree();
        comicCacheService.comicsTopDislikesThree();
        comicCacheService.comicsTopDislikesThree();

        // 그러므로 실제 DB 조회는 한번만 일어나야한다
        verify(comicRepository, times(1)).findTopDislikesThree();
    }


}