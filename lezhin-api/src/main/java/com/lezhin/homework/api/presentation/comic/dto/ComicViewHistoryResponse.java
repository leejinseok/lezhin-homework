package com.lezhin.homework.api.presentation.comic.dto;

import com.lezhin.homework.api.presentation.member.dto.MemberResponse;
import com.lezhin.homework.core.db.domain.comic.search.ComicViewHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ComicViewHistoryResponse {

    private long id;
    private ComicResponse comic;
    private MemberResponse member;
    private LocalDateTime viewDateTime;

    public static ComicViewHistoryResponse create(final ComicViewHistory history) {
        return of(
                history.getId(),
                ComicResponse.create(history.getComic()),
                MemberResponse.create(history.getMember()),
                history.getViewDateTime()
        );
    }
}

