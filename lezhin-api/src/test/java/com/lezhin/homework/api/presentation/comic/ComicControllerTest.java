package com.lezhin.homework.api.presentation.comic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lezhin.homework.api.application.config.ApiSecurityConfig;
import com.lezhin.homework.api.application.config.jwt.JwtConfig;
import com.lezhin.homework.api.application.config.jwt.JwtProvider;
import com.lezhin.homework.api.application.domain.comic.ComicCacheService;
import com.lezhin.homework.api.application.util.AES256Util;
import com.lezhin.homework.core.db.domain.author.Author;
import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ComicController.class})
@Import({ApiSecurityConfig.class, JwtConfig.class})
class ComicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProvider jwtProvider;

    @MockBean
    private ComicCacheService comicCacheService;

    @DisplayName("좋아요 많은순으로 3개")
    @Test
    void getByLikesDescThree() throws Exception {
        Member member = Member.builder()
                .id(1L)
                .userEmail(AES256Util.encrypt("lezhin@lezhin.com"))
                .build();
        String token = jwtProvider.createToken(member);

        List<Comic> comics = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Author author = Author.builder().id((long) i).nickname("작가" + i).build();
            Comic build = Comic.builder()
                    .id((long) i)
                    .dislikes(0)
                    .likes(100 - i)
                    .coin(new BigDecimal(0))
                    .openDate(LocalDate.now())
                    .author(author)
                    .build();
            comics.add(build);
        }
        when(comicCacheService.comicsTopLikesThree()).thenReturn(comics);

        mockMvc.perform(
                        get("/api/v1/comics/top-three-by-likes")
                                .header("Authorization", "Bearer " + token)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").exists())
                .andExpect(jsonPath("$.[0].dislikes").exists())
                .andExpect(jsonPath("$.[0].likes").exists())
                .andExpect(jsonPath("$.[0].coin").exists())
                .andExpect(jsonPath("$.[0].openDate").exists())
                .andExpect(jsonPath("$.[0].author").exists())
                .andExpect(jsonPath("$.[0].author.id").exists())
                .andExpect(jsonPath("$.[0].author.nickname").exists());
    }

    @DisplayName("싫어요 많은순으로 3개")
    @Test
    void getByDislikesDescThree() throws Exception {
        Member member = Member.builder()
                .id(1L)
                .userEmail(AES256Util.encrypt("lezhin@lezhin.com"))
                .build();
        String token = jwtProvider.createToken(member);

        List<Comic> comics = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Author author = Author.builder().id((long) i).nickname("작가" + i).build();
            Comic build = Comic.builder()
                    .id((long) i)
                    .dislikes(100 - i)
                    .likes(0)
                    .coin(new BigDecimal(0))
                    .openDate(LocalDate.now())
                    .author(author)
                    .build();
            comics.add(build);
        }
        when(comicCacheService.comicsTopDislikesThree()).thenReturn(comics);

        mockMvc.perform(
                        get("/api/v1/comics/top-three-by-dislikes")
                                .header("Authorization", "Bearer " + token)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").exists())
                .andExpect(jsonPath("$.[0].dislikes").exists())
                .andExpect(jsonPath("$.[0].likes").exists())
                .andExpect(jsonPath("$.[0].coin").exists())
                .andExpect(jsonPath("$.[0].openDate").exists())
                .andExpect(jsonPath("$.[0].author").exists())
                .andExpect(jsonPath("$.[0].author.id").exists())
                .andExpect(jsonPath("$.[0].author.nickname").exists());
    }

}