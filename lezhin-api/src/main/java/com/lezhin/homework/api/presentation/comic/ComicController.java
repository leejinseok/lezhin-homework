package com.lezhin.homework.api.presentation.comic;

import com.lezhin.homework.api.application.config.jwt.MemberToken;
import com.lezhin.homework.api.application.domain.comic.ComicCacheService;
import com.lezhin.homework.api.application.domain.comic.ComicService;
import com.lezhin.homework.api.application.domain.comic.ComicViewHistoryService;
import com.lezhin.homework.api.presentation.comic.dto.ComicResponse;
import com.lezhin.homework.api.presentation.comic.dto.ComicViewHistoryResponse;
import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.comic.search.ComicViewHistory;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "COMIC")
@RestController
@RequestMapping("/api/v1/comics")
@RequiredArgsConstructor
public class ComicController {

    private final ComicService comicService;
    private final ComicCacheService comicCacheService;
    private final ComicViewHistoryService comicViewHistoryService;

    @GetMapping("/top-three-by-likes")
    public ResponseEntity<List<ComicResponse>> getByLikesDescThree() {
        List<Comic> comics = comicCacheService.comicsTopLikesThree();
        List<ComicResponse> body = comics.stream().map(ComicResponse::create).toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(body);
    }

    @GetMapping("top-three-by-dislikes")
    public ResponseEntity<List<ComicResponse>> getByDislikesDescThree() {
        List<Comic> comics = comicCacheService.comicsTopDislikesThree();
        List<ComicResponse> body = comics.stream().map(ComicResponse::create).toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(body);
    }

    @GetMapping("/{comicId}")
    public ResponseEntity<ComicResponse> getComic(
            @PathVariable final long comicId,
            @AuthenticationPrincipal final MemberToken memberToken
    ) {
        Comic comic = comicService.findById(comicId);
        comicViewHistoryService.viewComic(comic, memberToken.getId());

        ComicResponse body = ComicResponse.create(comic);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(body);
    }

    @GetMapping("/{comicId}/view-histories")
    public ResponseEntity<Page<ComicViewHistoryResponse>> getComicViewHistories(
            @PathVariable final long comicId,
            @Parameter(name = "pageNo", example = "0") final int pageNo,
            @Parameter(name = "pageSize", example = "10") final int pageSize
    ) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        Page<ComicViewHistory> page = comicViewHistoryService.findAllByComicId(comicId, pageRequest);
        Page<ComicViewHistoryResponse> body = page.map(ComicViewHistoryResponse::create);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(body);
    }

}
