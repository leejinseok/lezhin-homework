package com.lezhin.homework.api.application.domain.member;

import com.lezhin.homework.api.application.config.ApiDbConfig;
import com.lezhin.homework.core.db.domain.Gender;
import com.lezhin.homework.core.db.domain.author.Author;
import com.lezhin.homework.core.db.domain.author.AuthorRepository;
import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.comic.ComicRepository;
import com.lezhin.homework.core.db.domain.comic.ComicType;
import com.lezhin.homework.core.db.domain.comic.rate.ComicMemberRate;
import com.lezhin.homework.core.db.domain.comic.rate.ComicMemberRateRepository;
import com.lezhin.homework.core.db.domain.comic.view.ComicViewHistory;
import com.lezhin.homework.core.db.domain.comic.view.ComicViewHistoryRepository;
import com.lezhin.homework.core.db.domain.member.Member;
import com.lezhin.homework.core.db.domain.member.MemberRepository;
import com.lezhin.homework.core.db.domain.member.MemberType;
import org.junit.jupiter.api.AfterEach;
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
import java.util.List;

import static com.lezhin.homework.api.application.domain.author.AuthorFactory.createSampleAuthor;
import static com.lezhin.homework.api.application.domain.comic.ComicFactory.createSampleComic;
import static com.lezhin.homework.api.application.domain.member.MemberFactory.createSampleMember;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles({"test"})
@DataJpaTest
@Import({ApiDbConfig.class})
class MemberServiceTest {

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private ComicMemberRateRepository comicMemberRateRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ComicViewHistoryRepository comicViewHistoryRepository;

    private static final int MEMBER_LENGTH = 11;
    private static final int VIEW_COUNT = 3;

    @AfterEach
    void tearDown() {
        comicViewHistoryRepository.deleteAll();
        comicMemberRateRepository.deleteAll();
        memberRepository.deleteAll();
        comicRepository.deleteAll();
        authorRepository.deleteAll();
    }

    MemberService createMemberService() {
        return new MemberService(memberRepository);
    }

    @DisplayName("최근 일주일간 등록한 사용자중 성인물을 3회이상 조회한 사용자를 조회")
    @Test
    void findAllMemberViewedAdultComicMoreThanThreeTimesAndRegisteredBetween() {
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
                ComicViewHistory comicViewHistory = ComicViewHistory.of(
                        viewDateTime
                );
                comicViewHistory.setMember(member);
                comicViewHistory.setComic(adultComic);

                comicViewHistoryRepository.save(comicViewHistory);
                viewDateTime = viewDateTime.plusMinutes(1);
            }
        }

        MemberService memberService = createMemberService();
        Page<Member> page = memberService.findAllMemberViewedAdultComicMoreThanThreeTimesAndRegisteredInAWeek(PageRequest.of(0, 10));
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(MEMBER_LENGTH);
    }

    @DisplayName("사용자 삭제 (평가, 조회이력 함께 삭졔)")
    @Test
    void deleteById() {
        Author author = authorRepository.save(createSampleAuthor(null));
        Comic comic = comicRepository.save(createSampleComic(null, author, new BigDecimal(0)));
        Member member = memberRepository.save(createSampleMember(null));

        ComicMemberRate comicMemberRate = ComicMemberRate.of(true, "재미있구만");
        comicMemberRate.setMember(member);
        comicMemberRate.setComic(comic);

        comicMemberRateRepository.save(comicMemberRate);

        ComicViewHistory viewHistory = ComicViewHistory.of(LocalDateTime.now());
        viewHistory.setMember(member);
        viewHistory.setComic(comic);
        comicViewHistoryRepository.save(viewHistory);

        MemberService memberService = createMemberService();
        memberService.deleteById(member.getId());

        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(0);

        List<ComicMemberRate> rates = comicMemberRateRepository.findAll();
        assertThat(rates.size()).isEqualTo(0);

        List<ComicViewHistory> viewHistories = comicViewHistoryRepository.findAll();
        assertThat(viewHistories.size()).isEqualTo(0);
    }

}