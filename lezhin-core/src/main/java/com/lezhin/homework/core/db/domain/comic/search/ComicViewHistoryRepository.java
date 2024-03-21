package com.lezhin.homework.core.db.domain.comic.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ComicViewHistoryRepository extends JpaRepository<ComicViewHistory, Long> {

    @Query("select comicViewHistory from ComicViewHistory comicViewHistory join fetch comicViewHistory.comic join fetch comicViewHistory.member where comicViewHistory.comic.id = :comicId")
    Page<ComicViewHistory> findAllByComicId(long comicId, Pageable pageable);
}
