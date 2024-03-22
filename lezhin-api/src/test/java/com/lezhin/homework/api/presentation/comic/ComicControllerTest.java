package com.lezhin.homework.api.presentation.comic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lezhin.homework.api.application.config.ApiSecurityConfig;
import com.lezhin.homework.api.application.config.jwt.JwtConfig;
import com.lezhin.homework.api.application.config.jwt.JwtProvider;
import com.lezhin.homework.api.application.domain.comic.ComicCacheService;
import com.lezhin.homework.api.application.domain.comic.ComicService;
import com.lezhin.homework.api.application.domain.comic.ComicViewHistoryService;
import com.lezhin.homework.api.application.util.AES256Util;
import com.lezhin.homework.core.db.domain.author.Author;
import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.comic.view.ComicViewHistory;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.lezhin.homework.api.application.domain.author.AuthorFactory.createSampleAuthor;
import static com.lezhin.homework.api.application.domain.comic.ComicFactory.createSampleComic;
import static com.lezhin.homework.api.application.domain.member.MemberFactory.createSampleMember;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
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
    private JwtProvider jwtProvider;

    @MockBean
    private ComicCacheService comicCacheService;

    @MockBean
    private ComicService comicService;

    @MockBean
    private ComicViewHistoryService comicViewHistoryService;

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

    @DisplayName("웹툰 조회")
    @Test
    void getComic() throws Exception {
        Member sampleMember = createSampleMember(1L);
        String token = jwtProvider.createToken(sampleMember);

        Author sampleAuthor = createSampleAuthor(1L);
        Comic sampleComic = createSampleComic(1L, sampleAuthor, new BigDecimal(0));
        when(comicService.findById(anyLong())).thenReturn(sampleComic);
        doNothing().when(comicViewHistoryService).saveViewHistory(any(), anyLong());

        mockMvc.perform(
                        get("/api/v1/comics/1")
                                .header("Authorization", "Bearer " + token)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleComic.getId()))
                .andExpect(jsonPath("$.contentsName").value(sampleComic.getContentsName()))
                .andExpect(jsonPath("$.author.id").value(sampleAuthor.getId()))
                .andExpect(jsonPath("$.author.nickname").value(sampleAuthor.getNickname()))
                .andExpect(jsonPath("$.coin").value(sampleComic.getCoin().toBigInteger()))
                .andExpect(jsonPath("$.openDate").exists())
                .andExpect(jsonPath("$.likes").value(sampleComic.getLikes()))
                .andExpect(jsonPath("$.dislikes").value(sampleComic.getDislikes()));
    }

    @DisplayName("웹툰 조회내역 조회")
    @Test
    void getComicViewHistories() throws Exception {
        Member member = createSampleMember(1L);
        String token = jwtProvider.createToken(member);

        Author author = createSampleAuthor(1L);
        Comic comic = createSampleComic(1L, author, new BigDecimal(0));

        List<ComicViewHistory> content = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ComicViewHistory viewHistory = ComicViewHistory.builder()
                    .id(1L)
                    .member(member)
                    .comic(comic)
                    .viewDateTime(LocalDateTime.now())
                    .build();
            content.add(viewHistory);
        }

        PageRequest pageRequest = PageRequest.of(0, 10);
        when(comicViewHistoryService.findAllByComicId(anyLong(), any())).thenReturn(
                new PageImpl<>(content, pageRequest, 10)
        );

        mockMvc.perform(
                        get("/api/v1/comics/1/view-histories")
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
                .andExpect(jsonPath("$.list[0].comic.id").exists())
                .andExpect(jsonPath("$.list[0].comic.contentsName").exists())
                .andExpect(jsonPath("$.list[0].comic.author.id").exists())
                .andExpect(jsonPath("$.list[0].comic.author.nickname").exists())
                .andExpect(jsonPath("$.list[0].comic.coin").exists())
                .andExpect(jsonPath("$.list[0].comic.openDate").exists())
                .andExpect(jsonPath("$.list[0].comic.likes").exists())
                .andExpect(jsonPath("$.list[0].comic.dislikes").exists())
                .andExpect(jsonPath("$.list[0].member.id").exists())
                .andExpect(jsonPath("$.list[0].member.userName").exists())
                .andExpect(jsonPath("$.list[0].member.userEmail").exists())
                .andExpect(jsonPath("$.list[0].member.gender").exists())
                .andExpect(jsonPath("$.list[0].member.type").exists())
                .andExpect(jsonPath("$.list[0].member.registerDateTime").exists())
                .andExpect(jsonPath("$.list[0].viewDateTime").exists());
    }

}