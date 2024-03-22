package com.lezhin.homework.api.application.domain.member;

import com.lezhin.homework.api.application.config.ApiDbConfig;
import com.lezhin.homework.core.db.domain.Gender;
import com.lezhin.homework.core.db.domain.author.Author;
import com.lezhin.homework.core.db.domain.author.AuthorRepository;
import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.comic.ComicRepository;
import com.lezhin.homework.core.db.domain.comic.ComicType;
import com.lezhin.homework.core.db.domain.comic.view.ComicViewHistory;
import com.lezhin.homework.core.db.domain.comic.view.ComicViewHistoryRepository;
import com.lezhin.homework.core.db.domain.member.Member;
import com.lezhin.homework.core.db.domain.member.MemberRepository;
import com.lezhin.homework.core.db.domain.member.MemberType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.lezhin.homework.api.application.domain.author.AuthorFactory.createSampleAuthor;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles({"test"})
@DataJpaTest
@Import({ApiDbConfig.class})
class MemberServiceTest {

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ComicViewHistoryRepository comicViewHistoryRepository;

    private static final int MEMBER_LENGTH = 11;
    private static final int VIEW_COUNT = 3;

    @BeforeEach
    void setUp() {
        Author author = createSampleAuthor(null);
        authorRepository.save(author);

        Comic adultComic = Comic.builder()
                .contentsName("성인만화")
                .type(ComicType.ADULT)
                .likes(0)
                .dislikes(0)
                .author(author)
                .coin(new BigDecimal(0))
                .build();

        comicRepository.save(adultComic);

        // 성인물 조회이력 남기기
        for (int i = 0; i < MEMBER_LENGTH; i++) {
            Member member = Member.builder()
                    .type(MemberType.ADULT)
                    .registerDateTime(LocalDateTime.now().minusDays(1))
                    .password("password")
                    .gender(Gender.MALE)
                    .userName("이름" + i)
                    .userEmail("email" + i)
                    .build();

            memberRepository.save(member);

            LocalDateTime viewDateTime = LocalDateTime.now();
            for (int j = 0; j < VIEW_COUNT; j++) {
                ComicViewHistory comicViewHistory = ComicViewHistory.create(
                        member,
                        adultComic,
                        viewDateTime
                );
                comicViewHistoryRepository.save(comicViewHistory);
                viewDateTime = viewDateTime.plusMinutes(1);
            }
        }
    }

    @AfterEach
    void tearDown() {
        comicViewHistoryRepository.deleteAll();
        comicRepository.deleteAll();
        authorRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @DisplayName("최근 일주일간 등록한 사용자중 성인물을 3회이상 조회한 사용자를 조회")
    @Test
    void findAllMemberViewedAdultComicMoreThanThreeTimesAndRegisteredBetween() {
        MemberService memberService = new MemberService(memberRepository);
        Page<Member> page = memberService.findAllMemberViewedAdultComicMoreThanThreeTimesAndRegisteredInAWeek(PageRequest.of(0, 10));
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(MEMBER_LENGTH);
    }
}