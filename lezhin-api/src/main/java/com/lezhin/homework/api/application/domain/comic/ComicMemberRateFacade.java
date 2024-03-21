package com.lezhin.homework.api.application.domain.comic;

import com.lezhin.homework.api.application.lock.MysqlLockService;
import com.lezhin.homework.api.presentation.comic.dto.ComicMemberRateRequest;
import com.lezhin.homework.core.db.domain.comic.rate.ComicMemberRate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ComicMemberRateFacade {

    private static final String LOCK_KEY_PREFIX = "LOCK:COMIC_RATE_AND_COUNT_LIKES_AND_DISLIKES:";

    private final MysqlLockService mysqlLockService;
    private final ComicMemberRateService comicMemberRateService;

    public ComicMemberRate rateComicWithLock(final long memberId, final ComicMemberRateRequest request) {
        long comicId = request.getComicId();
        String lockKey = LOCK_KEY_PREFIX + comicId;
        boolean lock = mysqlLockService.getLock(lockKey, 5);
        if (lock) {
            ComicMemberRate comicMemberRate = comicMemberRateService.rateComic(memberId, request);
            comicMemberRateService.countComicLikeAndDislikeCount(comicId);
            mysqlLockService.releaseLock(lockKey);
            return comicMemberRate;
        } else {
            throw new RuntimeException("ComicMemberRateFacade > rateComicWithLock : lock 획득 실패");
        }
    }

}
