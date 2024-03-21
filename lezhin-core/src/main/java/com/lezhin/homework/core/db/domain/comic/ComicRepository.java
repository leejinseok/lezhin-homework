package com.lezhin.homework.core.db.domain.comic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComicRepository extends JpaRepository<Comic, Long> {

    @Query("select comic from Comic comic order by comic.likes desc limit 3")
    List<Comic> findTopLikesThree();

    @Query("select comic from Comic comic order by comic.dislikes desc limit 3")
    List<Comic> findTopDislikesThree();

}
