package com.lezhin.homework.api.presentation.member;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Member")
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {


}
