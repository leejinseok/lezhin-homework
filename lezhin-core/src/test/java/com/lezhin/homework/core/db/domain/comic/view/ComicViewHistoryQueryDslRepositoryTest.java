package com.lezhin.homework.core.db.domain.comic.view;

import com.lezhin.homework.core.db.CoreTestConfiguration;
import com.lezhin.homework.core.db.domain.Gender;
import com.lezhin.homework.core.db.domain.author.Author;
import com.lezhin.homework.core.db.domain.author.AuthorRepository;
import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.comic.ComicRepository;
import com.lezhin.homework.core.db.domain.comic.ComicType;
import com.lezhin.homework.core.db.domain.member.Member;
import com.lezhin.homework.core.db.domain.member.MemberRepository;
import com.lezhin.homework.core.db.domain.member.MemberType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.lezhin.homework.core.db.domain.author.AuthorFactory.createSampleAuthor;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles({"core-db", "test"})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = CoreTestConfiguration.class)
class ComicViewHistoryQueryDslRepositoryTest {

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ComicViewHistoryRepository comicViewHistoryRepository;

    @Autowired
    private ComicViewHistoryQueryDslRepository comicViewHistoryQueryDslRepository;

    @BeforeEach
    void setUp() {
        Author author = createSampleAuthor();
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
        for (int i = 0; i < 11; i++) {
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
            for (int j = 0; j < 2; j++) {
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

    @DisplayName("특정 기간동안 성인물을 조회한 사용자를 조회")
    @Test
    void findAllMemberAdultComicVisitDateTimeBetween() {
        Page<Member> page = comicViewHistoryQueryDslRepository.findAllMemberAdultComicVisitDateTimeBetween(
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(1),
                PageRequest.of(0, 10)
        );

        assertThat(page.getTotalElements()).isEqualTo(11);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

}