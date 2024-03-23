package com.lezhin.homework.api.presentation.member;

import com.lezhin.homework.api.application.domain.member.MemberService;
import com.lezhin.homework.api.presentation.common.dto.PageResponse;
import com.lezhin.homework.api.presentation.member.dto.MemberResponse;
import com.lezhin.homework.core.db.domain.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "사용자 (Member)")
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "최근 일주일 가입자중 성인컨텐츠 3회 이상 조회한 회원", description = "최근 일주일 가입자중 성인컨텐츠 3회 이상 조회")
    @GetMapping(params = {"searchType=viewed-adult-comics-more-than-three-times-and-registered-in-a-week"})
    public ResponseEntity<PageResponse<MemberResponse>> getAllMemberViewedAdultComicMoreThanThreeTimesAndRegisteredInAWeek(
            @Parameter(name = "pageNo", example = "0") final int pageNo,
            @Parameter(name = "pageSize", example = "10") final int pageSize,
            @Parameter(name = "searchType") @RequestParam(defaultValue = "viewed-adult-comics-more-than-three-times-and-registered-in-a-week") final String searchType
    ) {
        Page<Member> page = memberService.findAllMemberViewedAdultComicMoreThanThreeTimesAndRegisteredInAWeek(PageRequest.of(pageNo, pageSize));
        Page<MemberResponse> map = page.map(MemberResponse::create);
        PageResponse<MemberResponse> body = new PageResponse<>(map);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(body);
    }

    @Operation(summary = "사용자 삭제", description = "사용자 삭제 (평가내역, 조회이력 포함)")
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMemberById(
            @PathVariable final long memberId
    ) {
        memberService.deleteById(memberId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
