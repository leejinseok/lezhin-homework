package com.lezhin.homework.api.presentation.comic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lezhin.homework.api.application.config.ApiSecurityConfig;
import com.lezhin.homework.api.application.config.jwt.JwtConfig;
import com.lezhin.homework.api.application.config.jwt.JwtProvider;
import com.lezhin.homework.api.application.domain.comic.ComicMemberRateFacade;
import com.lezhin.homework.api.application.util.AES256Util;
import com.lezhin.homework.api.presentation.comic.dto.ComicMemberRateRequest;
import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.comic.rate.ComicMemberRate;
import com.lezhin.homework.core.db.domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = {ComicMemberRateController.class})
@Import({ApiSecurityConfig.class, JwtConfig.class})
class ComicMemberRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProvider jwtProvider;

    @MockBean
    private ComicMemberRateFacade comicMemberRateFacade;

    @DisplayName("웹툰 평가")
    @Test
    void rateComic() throws Exception {
        Member member = Member.builder()
                .id(1L)
                .userEmail(AES256Util.encrypt("lezhin@lezhin.com"))
                .build();
        String token = jwtProvider.createToken(member);

        ComicMemberRateRequest request = ComicMemberRateRequest.of(1L, true, "재미있구만");
        byte[] content = objectMapper.writeValueAsBytes(request);

        Comic comic = Comic.builder()
                .id(1L)
                .coin(new BigDecimal(0))
                .contentsName("제목")
                .likes(1L)
                .dislikes(1L)
                .build();

        when(comicMemberRateFacade.rateComicWithLock(anyLong(), any())).thenReturn(
                ComicMemberRate.create(comic, member, request.isLike(), request.getComment())
        );

        mockMvc.perform(
                post("/api/v1/comic-member-rate")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
        ).andDo(print());
    }

}