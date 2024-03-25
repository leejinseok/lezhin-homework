package com.lezhin.homework.api.application.domain.comic;

import com.lezhin.homework.api.application.config.ApiDbConfig;
import com.lezhin.homework.api.exception.BadRequestException;
import com.lezhin.homework.api.presentation.comic.dto.ComicRequest;
import com.lezhin.homework.core.db.domain.author.Author;
import com.lezhin.homework.core.db.domain.author.AuthorRepository;
import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.comic.ComicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;

import static com.lezhin.homework.api.application.domain.author.AuthorFactory.createSampleAuthor;
import static com.lezhin.homework.api.application.domain.comic.ComicFactory.createSampleComic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles({"test"})
@DataJpaTest
@Import({ApiDbConfig.class, ComicService.class})
class ComicServiceTest {

    @Autowired
    private ComicService comicService;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ComicRepository comicRepository;

    private long comicId;

    @BeforeEach
    void setUp() {
        Author sampleAuthor = createSampleAuthor(null);
        authorRepository.save(sampleAuthor);

        Comic save = comicRepository.save(
                createSampleComic(null, sampleAuthor, new BigDecimal(0))
        );
        comicId = save.getId();
    }

    @DisplayName("유료/무료 전환 테스트")
    @Test
    void updateComicCoin() {
        // 유료 전환
        ComicRequest requestToPay = ComicRequest.of(new BigDecimal(120));
        Comic payComic = comicService.updateComic(comicId, requestToPay);
        assertThat(payComic.getCoin().toString()).isEqualTo(requestToPay.getCoin().toString());

        // 무료 전환
        ComicRequest requestToFree = ComicRequest.of(new BigDecimal(0));
        Comic freeComic = comicService.updateComic(comicId, requestToFree);
        assertThat(freeComic.getCoin().toString()).isEqualTo(requestToFree.getCoin().toString());
    }

    @DisplayName("유료/무료 전환 테스트 (유효하지 않은 유료 금액)")
    @Test
    void updateComicCoinNotValidPaidCoin() {
        // 500원 초과
        ComicRequest requestToPay = ComicRequest.of(new BigDecimal(501));
        assertThrows(BadRequestException.class, () -> {
            comicService.updateComic(comicId, requestToPay);
        });

        // 100원 미만
        ComicRequest requestToPayUnder100 = ComicRequest.of(new BigDecimal(99));
        assertThrows(BadRequestException.class, () -> {
            comicService.updateComic(comicId, requestToPayUnder100);
        });

    }

    @DisplayName("유료 가격 검증")
    @Test
    void validCoinRange() {
        assertThat(comicService.validPaidCoinRange(new BigDecimal(99))).isFalse();
        assertThat(comicService.validPaidCoinRange(new BigDecimal(100))).isTrue();
        assertThat(comicService.validPaidCoinRange(new BigDecimal(200))).isTrue();
        assertThat(comicService.validPaidCoinRange(new BigDecimal(500))).isTrue();
        assertThat(comicService.validPaidCoinRange(new BigDecimal(501))).isFalse();
    }

}