package com.lezhin.homework.api.application.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class ApiCacheConfig {

    public static final String TOP_LIKES_COMICS = "topLikesComics";
    public static final String TOP_DISLIKES_COMICS = "topDislikesComics";

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
