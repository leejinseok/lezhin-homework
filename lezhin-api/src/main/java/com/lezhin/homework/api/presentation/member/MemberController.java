package com.lezhin.homework.api.presentation.member;

import com.lezhin.homework.api.application.domain.member.MemberService;
import com.lezhin.homework.api.presentation.common.dto.PageResponse;
import com.lezhin.homework.api.presentation.member.dto.MemberResponse;
import com.lezhin.homework.core.db.domain.member.Member;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Member")
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping(params = {"searchType=viewed-adult-comics-more-than-three-times-and-registered-in-a-week"})
    public ResponseEntity<PageResponse<MemberResponse>> getAllMemberViewedAdultComicMoreThanThreeTimesAndRegisteredInAWeek(
            @Parameter(name = "pageNo", example = "0") final int pageNo,
            @Parameter(name = "pageSize", example = "10") final int pageSize
    ) {
        Page<Member> page = memberService.findAllMemberViewedAdultComicMoreThanThreeTimesAndRegisteredInAWeek(PageRequest.of(pageNo, pageSize));
        Page<MemberResponse> map = page.map(MemberResponse::create);
        PageResponse<MemberResponse> body = new PageResponse<>(map);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(body);
    }

}
