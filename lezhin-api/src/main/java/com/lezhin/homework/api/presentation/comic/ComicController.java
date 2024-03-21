package com.lezhin.homework.api.presentation.comic;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "COMIC")
@RestController
@RequestMapping("/api/v1/comics")
@RequiredArgsConstructor
public class ComicController {
}
