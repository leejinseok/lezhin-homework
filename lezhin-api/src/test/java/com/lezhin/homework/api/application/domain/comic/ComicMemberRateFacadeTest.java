package com.lezhin.homework.api.application.domain.comic;

import com.lezhin.homework.api.application.config.ApiDbConfig;
import com.lezhin.homework.api.application.lock.PseudoLockService;
import com.lezhin.homework.api.presentation.comic.dto.ComicMemberRateRequest;
import com.lezhin.homework.core.db.domain.Gender;
import com.lezhin.homework.core.db.domain.author.Author;
import com.lezhin.homework.core.db.domain.author.AuthorRepository;
import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.comic.ComicRepository;
import com.lezhin.homework.core.db.domain.comic.rate.ComicMemberRate;
import com.lezhin.homework.core.db.domain.member.Member;
import com.lezhin.homework.core.db.domain.member.MemberRepository;
import com.lezhin.homework.core.db.domain.member.MemberType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles({"test"})
@DataJpaTest
@Import({ApiDbConfig.class})
@ContextConfiguration(classes = {ComicMemberRateFacade.class, ComicMemberRateService.class, PseudoLockService.class})
class ComicMemberRateFacadeTest {

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ComicMemberRateFacade comicMemberRateFacade;

    @DisplayName("웹툰 평가")
    @Test
    void rateComic() {
        Member member = Member.builder()
                .userEmail("lezhin@lezhin.com")
                .userName("김레진")
                .password("password")
                .type(MemberType.NORMAL)
                .gender(Gender.MALE)
                .registerDateTime(LocalDateTime.now())
                .build();

        memberRepository.save(member);

        Author author = Author.builder()
                .nickname("김작가")
                .build();

        authorRepository.save(author);

        Comic comic = Comic
                .builder()
                .author(author)
                .contentsName("키다리 아저씨")
                .openDate(LocalDate.now())
                .likes(0L)
                .dislikes(0L)
                .coin(new BigDecimal(0))
                .build();

        comicRepository.save(comic);


        ComicMemberRateRequest request = ComicMemberRateRequest.of(
                comic.getId(), true, "재미있구만"
        );
//        ComicMemberRateFacade comicMemberRateFacade = createComicMemberRateFacade();
        ComicMemberRate comicMemberRate = comicMemberRateFacade.rateComicWithLock(
                member.getId(),
                request
        );

        Long likes = comic.getLikes();
        assertThat(likes).isEqualTo(1L);
    }

}