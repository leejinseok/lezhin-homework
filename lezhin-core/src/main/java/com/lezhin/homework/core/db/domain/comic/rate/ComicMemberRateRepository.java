package com.lezhin.homework.core.db.domain.comic.rate;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComicMemberRateRepository extends JpaRepository<ComicMemberRate, Long> {

    List<ComicMemberRate> findAllByComicId(long comicId);
}
