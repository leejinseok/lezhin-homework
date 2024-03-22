package com.lezhin.homework.api.application.domain.comic;

import com.lezhin.homework.api.application.config.ApiDbConfig;
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

import java.math.BigDecimal;

import static com.lezhin.homework.api.application.domain.author.AuthorFactory.createSampleAuthor;
import static com.lezhin.homework.api.application.domain.comic.ComicFactory.createSampleComic;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles({"test"})
@DataJpaTest
@Import({ApiDbConfig.class})
class ComicServiceTest {

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private AuthorRepository authorRepository;

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
        ComicService comicService = new ComicService(comicRepository);

        // 유료 전환
        ComicRequest requestToPay = ComicRequest.of(new BigDecimal(10));
        Comic payComic = comicService.updateComic(comicId, requestToPay);
        assertThat(payComic.getCoin().toString()).isEqualTo(requestToPay.getCoin().toString());

        // 무료 전환
        ComicRequest requestToFree = ComicRequest.of(new BigDecimal(0));
        Comic freeComic = comicService.updateComic(comicId, requestToFree);
        assertThat(freeComic.getCoin().toString()).isEqualTo(requestToFree.getCoin().toString());

    }

}