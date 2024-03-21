package com.lezhin.homework.api.application.domain.comic;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.comic.ComicRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.lezhin.homework.api.application.config.ApiCacheConfig.TOP_DISLIKES_COMICS;
import static com.lezhin.homework.api.application.config.ApiCacheConfig.TOP_LIKES_COMICS;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
class ComicCacheServiceTest {

    @Mock
    private ComicRepository comicRepository;

    @Autowired
    private CacheManager cacheManager;

    @Configuration
    @EnableCaching
    static class CacheConfig {

        @Bean
        public CacheManager cacheManager() {
            SimpleCacheManager cacheManager = new SimpleCacheManager();

            List<CaffeineCache> caches = new ArrayList<>();
            caches.add(topLikesComicsCache());
            caches.add(topDislikesComicsCache());

            cacheManager.setCaches(caches);
            return cacheManager;
        }

        private CaffeineCache topLikesComicsCache() {
            return new CaffeineCache(TOP_LIKES_COMICS,
                    Caffeine.newBuilder().recordStats()
                            .expireAfterWrite(10, TimeUnit.MINUTES)
                            .maximumSize(10)
                            .build()
            );
        }

        private CaffeineCache topDislikesComicsCache() {
            return new CaffeineCache(TOP_DISLIKES_COMICS,
                    Caffeine.newBuilder().recordStats()
                            .expireAfterWrite(10, TimeUnit.MINUTES)
                            .maximumSize(10)
                            .build()
            );
        }
    }

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
        ComicCacheService comicCacheService = new ComicCacheService(cacheManager, comicRepository);
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
        ComicCacheService comicCacheService = new ComicCacheService(cacheManager, comicRepository);
        comicCacheService.comicsTopDislikesThree();
        comicCacheService.comicsTopDislikesThree();
        comicCacheService.comicsTopDislikesThree();

        // 그러므로 실제 DB 조회는 한번만 일어나야한다
        verify(comicRepository, times(1)).findTopDislikesThree();
    }


}