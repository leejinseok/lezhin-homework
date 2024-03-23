package com.lezhin.homework.api.presentation.member;

import com.lezhin.homework.api.application.config.ApiSecurityConfig;
import com.lezhin.homework.api.application.config.jwt.JwtConfig;
import com.lezhin.homework.api.application.config.jwt.JwtProvider;
import com.lezhin.homework.api.application.domain.member.MemberService;
import com.lezhin.homework.core.db.domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.lezhin.homework.api.application.domain.member.MemberFactory.createSampleMember;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {MemberController.class})
@Import({ApiSecurityConfig.class, JwtConfig.class})
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtProvider jwtProvider;

    @MockBean
    private MemberService memberService;

    @DisplayName("최근 일주일간 등록한 사용자중 성인물을 3회이상 조회한 사용자를 조회")
    @Test
    void getAllMemberViewedAdultComicMoreThanThreeTimesAndRegisteredInAWeek() throws Exception {
        Member member = createSampleMember(1L);
        String token = jwtProvider.createToken(member);

        List<Member> members = List.of(
                createSampleMember(1L)
        );

        when(
                memberService.findAllMemberViewedAdultComicMoreThanThreeTimesAndRegisteredInAWeek(any())
        ).thenReturn(
                new PageImpl<>(members, PageRequest.of(0, 10), 1)
        );

        mockMvc.perform(
                        get("/api/v1/members")
                                .param("searchType", "viewed-adult-comics-more-than-three-times-and-registered-in-a-week")
                                .param("pageNo", "0")
                                .param("pageSize", "10")
                                .header("Authorization", "Bearer " + token)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageInfo.currentPage").exists())
                .andExpect(jsonPath("$.pageInfo.totalPages").exists())
                .andExpect(jsonPath("$.pageInfo.size").exists())
                .andExpect(jsonPath("$.pageInfo.totalElements").exists())
                .andExpect(jsonPath("$.pageInfo.sort").exists())
                .andExpect(jsonPath("$.pageInfo.last").exists())
                .andExpect(jsonPath("$.list[0].id").exists())
                .andExpect(jsonPath("$.list[0].userName").exists())
                .andExpect(jsonPath("$.list[0].userEmail").exists())
                .andExpect(jsonPath("$.list[0].gender").exists())
                .andExpect(jsonPath("$.list[0].type").exists())
                .andExpect(jsonPath("$.list[0].registerDateTime").exists());
    }

    @DisplayName("사용자 삭제")
    @Test
    void deleteMemberById() throws Exception {
        Member member = createSampleMember(1L);
        String token = jwtProvider.createToken(member);

        mockMvc.perform(
                        delete("/api/v1/members/1")
                                .header("Authorization", "Bearer " + token)
                )
                .andDo(print())
                .andExpect(status().isNoContent());

        doNothing().when(memberService).deleteById(anyLong());
        verify(memberService, times(1)).deleteById(anyLong());
    }
}