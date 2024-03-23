package com.lezhin.homework.api.presentation.comic;

import com.lezhin.homework.api.application.config.jwt.MemberToken;
import com.lezhin.homework.api.application.domain.comic.ComicMemberRateFacade;
import com.lezhin.homework.api.presentation.comic.dto.ComicMemberRateRequest;
import com.lezhin.homework.api.presentation.comic.dto.ComicMemberRateResponse;
import com.lezhin.homework.core.db.domain.comic.rate.ComicMemberRate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "웹툰 평가 (Comic Member Rate)")
@RestController
@RequestMapping("/api/v1/comic-member-rate")
@RequiredArgsConstructor
public class ComicMemberRateController {

    private final ComicMemberRateFacade comicMemberRateFacade;

    @Operation(summary = "평가 작성", description = "평가 작성")
    @PostMapping
    public ResponseEntity<ComicMemberRateResponse> rateComic(
            @RequestBody final ComicMemberRateRequest request,
            @AuthenticationPrincipal final MemberToken memberToken
    ) {
        ComicMemberRate comicMemberRate = comicMemberRateFacade.rateComicWithLock(memberToken.getId(), request);
        ComicMemberRateResponse body = ComicMemberRateResponse.create(comicMemberRate);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(body);
    }
}
