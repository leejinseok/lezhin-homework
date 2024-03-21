package com.lezhin.homework.core.db.domain.comic.rate;

import com.lezhin.homework.core.db.CoreTestConfiguration;
import com.lezhin.homework.core.db.domain.author.Author;
import com.lezhin.homework.core.db.domain.author.AuthorRepository;
import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.comic.ComicRepository;
import com.lezhin.homework.core.db.domain.member.Member;
import com.lezhin.homework.core.db.domain.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;

import static com.lezhin.homework.core.db.domain.author.AuthorFactory.createSampleAuthor;
import static com.lezhin.homework.core.db.domain.comic.ComicFactory.createSampleComic;
import static com.lezhin.homework.core.db.domain.member.MemberFactory.createSampleMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles({"core-db", "test"})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = CoreTestConfiguration.class)
class ComicMemberRateRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private ComicMemberRateRepository comicMemberRateRepository;

    private Comic comic;
    private Member member;

    @BeforeEach
    void setUp() {
        member = createSampleMember();
        memberRepository.save(member);

        Author author = createSampleAuthor();
        authorRepository.save(author);

        comic = createSampleComic(author, new BigDecimal(0));
        comicRepository.save(comic);
    }

    @DisplayName("동일 작품에 중복 평가 등록")
    @Test
    void saveDuplicatedRate() {
        ComicMemberRate comicMemberRate = ComicMemberRate.builder()
                .comic(comic)
                .member(member)
                .isLike(true)
                .comment("훌륭한 작품")
                .build();

        comicMemberRateRepository.save(comicMemberRate);

        ComicMemberRate duplicated = ComicMemberRate.builder()
                .comic(comic)
                .member(member)
                .isLike(true)
                .comment("훌륭한 작품")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            comicMemberRateRepository.save(duplicated);
        });
    }

    @DisplayName("좋아요/싫어요에 값이 없는 경우")
    @Test
    void saveWithoutIsLike() {
        ComicMemberRate comicMemberRate = ComicMemberRate.builder()
                .comic(comic)
                .member(member)
                .comment("훌륭한 작품")
                .build();

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            comicMemberRateRepository.save(comicMemberRate);
        });

        String message = exception.getMessage();
        assertThat(message).contains("not-null property references a null or transient value");
    }

}