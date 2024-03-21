package com.lezhin.homework.api.application.domain.comic;

import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.comic.ComicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.lezhin.homework.api.application.config.ApiCacheConfig.TOP_LIKES_COMICS;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComicCacheService {


    private final CacheManager cacheManager;
    private final ComicRepository comicRepository;

    public List<Comic> comicsTopLikesThree() {
        List<Comic> cache = getTopLikesComicCache();
        if (ObjectUtils.isNotEmpty(cache)) {
            return cache;
        }

        List<Comic> topLikesThree = comicRepository.findTopLikesThree();
        putTopLikesComicCache(topLikesThree);
        return topLikesThree;
    }

    private List<Comic> getTopLikesComicCache() {
        return (List<Comic>) cacheManager.getCache(TOP_LIKES_COMICS).get("LIST", List.class);
    }

    private void putTopLikesComicCache(List<Comic> comics) {
        cacheManager.getCache(TOP_LIKES_COMICS).put("LIST", comics);
    }

    public List<Comic> comicsTopDislikesThree() {
        return comicRepository.findTopDislikesThree();
    }
}
