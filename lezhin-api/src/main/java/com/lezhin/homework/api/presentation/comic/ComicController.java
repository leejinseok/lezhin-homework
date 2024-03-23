package com.lezhin.homework.api.presentation.comic;

import com.lezhin.homework.api.application.config.jwt.MemberToken;
import com.lezhin.homework.api.application.domain.comic.ComicCacheService;
import com.lezhin.homework.api.application.domain.comic.ComicService;
import com.lezhin.homework.api.application.domain.comic.ComicViewHistoryService;
import com.lezhin.homework.api.presentation.comic.dto.ComicRequest;
import com.lezhin.homework.api.presentation.comic.dto.ComicResponse;
import com.lezhin.homework.api.presentation.comic.dto.ComicViewHistoryResponse;
import com.lezhin.homework.api.presentation.common.dto.PageResponse;
import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.comic.view.ComicViewHistory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "웹툰 (Comic)")
@RestController
@RequestMapping("/api/v1/comics")
@RequiredArgsConstructor
public class ComicController {

    private final ComicService comicService;
    private final ComicCacheService comicCacheService;
    private final ComicViewHistoryService comicViewHistoryService;

    @Operation(summary = "좋아요 많은순으로 3개", description = "좋아요 많은순으로 3개")
    @GetMapping("/top-three-by-likes")
    public ResponseEntity<List<ComicResponse>> getByLikesDescThree() {
        List<Comic> comics = comicCacheService.comicsTopLikesThree();
        List<ComicResponse> body = comics.stream().map(ComicResponse::create).toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(body);
    }

    @Operation(summary = "싫어요 많은순으로 3개", description = "싫어요 많은순으로 3개")
    @GetMapping("top-three-by-dislikes")
    public ResponseEntity<List<ComicResponse>> getByDislikesDescThree() {
        List<Comic> comics = comicCacheService.comicsTopDislikesThree();
        List<ComicResponse> body = comics.stream().map(ComicResponse::create).toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(body);
    }

    @Operation(summary = "웹툰 조회 (단건)", description = "웹툰 조회 (단건)")
    @GetMapping("/{comicId}")
    public ResponseEntity<ComicResponse> getComic(
            @PathVariable final long comicId,
            @AuthenticationPrincipal final MemberToken memberToken
    ) {
        Comic comic = comicService.findById(comicId);
        comicViewHistoryService.saveViewHistory(comic, memberToken.getId());

        ComicResponse body = ComicResponse.create(comic);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(body);
    }

    @Operation(summary = "웹툰의 조회이력 조회", description = "웹툰의 조회이력 조회")
    @GetMapping("/{comicId}/view-histories")
    public ResponseEntity<PageResponse<ComicViewHistoryResponse>> getComicViewHistories(
            @PathVariable final long comicId,
            @Parameter(name = "pageNo", example = "0") final int pageNo,
            @Parameter(name = "pageSize", example = "10") final int pageSize
    ) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        Page<ComicViewHistory> page = comicViewHistoryService.findAllByComicId(comicId, pageRequest);
        Page<ComicViewHistoryResponse> map = page.map(ComicViewHistoryResponse::create);
        PageResponse<ComicViewHistoryResponse> body = new PageResponse<>(map);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(body);
    }

    @Operation(summary = "웹툰 등록", description = "웹툰 등록")
    @PostMapping
    public ResponseEntity<ComicResponse> saveComic(
            @RequestBody final ComicRequest request
    ) {
        Comic save = comicService.save(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ComicResponse.create(save));
    }

    @Operation(summary = "웹툰 수정 (유/무료)", description = "웹툰 수정 (유/무료)")
    @PatchMapping("/{comicId}")
    public ResponseEntity<ComicResponse> updateComic(
            @PathVariable final long comicId,
            @RequestBody final ComicRequest request
    ) {
        Comic comic = comicService.updateComic(comicId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ComicResponse.create(comic));
    }

}
