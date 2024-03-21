package com.lezhin.homework.api.application.domain.comic;

import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.comic.ComicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.lezhin.homework.api.application.config.ApiCacheConfig.TOP_DISLIKES_COMICS;
import static com.lezhin.homework.api.application.config.ApiCacheConfig.TOP_LIKES_COMICS;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComicService {



}
