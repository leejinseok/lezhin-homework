package com.lezhin.homework.api.presentation.comic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ComicMemberRateRequest {

    private long comicId;
    private boolean isLike;
    private String comment;

}
