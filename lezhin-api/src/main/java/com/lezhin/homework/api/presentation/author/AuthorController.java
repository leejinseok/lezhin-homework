package com.lezhin.homework.api.presentation.author;

import com.lezhin.homework.api.application.domain.author.AuthorService;
import com.lezhin.homework.api.presentation.author.dto.AuthorRequest;
import com.lezhin.homework.api.presentation.author.dto.AuthorResponse;
import com.lezhin.homework.core.db.domain.author.Author;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "만화작가 (Author)")
@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @Operation(summary = "작가 등록", description = "작가 등록")
    @PostMapping
    public ResponseEntity<AuthorResponse> save(
            @RequestBody final AuthorRequest authorRequest
    ) {
        Author author = authorService.save(authorRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AuthorResponse.create(author));
    }

}
