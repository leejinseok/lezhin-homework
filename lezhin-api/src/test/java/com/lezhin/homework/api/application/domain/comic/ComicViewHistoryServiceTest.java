package com.lezhin.homework.api.application.domain.comic;

import com.lezhin.homework.api.application.config.ApiDbConfig;
import com.lezhin.homework.core.db.domain.author.Author;
import com.lezhin.homework.core.db.domain.author.AuthorRepository;
import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.comic.ComicRepository;
import com.lezhin.homework.core.db.domain.comic.search.ComicViewHistory;
import com.lezhin.homework.core.db.domain.comic.search.ComicViewHistoryRepository;
import com.lezhin.homework.core.db.domain.member.Member;
import com.lezhin.homework.core.db.domain.member.MemberRepository;
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
class ComicViewHistoryServiceTest {

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private ComicViewHistoryRepository comicViewHistoryRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @AfterEach
    void teardown() {
        comicViewHistoryRepository.deleteAll();
        comicRepository.deleteAll();
        memberRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @DisplayName("웹툰 조회내역 저장")
    @Test
    void saveViewHistory() {
        Member sampleMember = createSampleMember();
        memberRepository.save(sampleMember);

        Author sampleAuthor = createSampleAuthor(null);
        authorRepository.save(sampleAuthor);

        Comic sampleComic = createSampleComic(null, sampleAuthor, new BigDecimal(0));
        comicRepository.save(sampleComic);

        ComicViewHistoryService service = new ComicViewHistoryService(comicViewHistoryRepository, memberRepository);
        service.saveViewHistory(sampleComic, sampleMember.getId());

        List<ComicViewHistory> all = comicViewHistoryRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
    }

    @DisplayName("웹툰 조회내역 조회")
    @Test
    void findAllByComicId() {
        Member sampleMember = createSampleMember();
        memberRepository.save(sampleMember);

        Author sampleAuthor = createSampleAuthor(null);
        authorRepository.save(sampleAuthor);

        Comic sampleComic = createSampleComic(null, sampleAuthor, new BigDecimal(0));
        comicRepository.save(sampleComic);

        for (int i = 0; i < 10; i++) {
            ComicViewHistory viewHistory = ComicViewHistory.create(
                    sampleMember, sampleComic, LocalDateTime.now()
            );

            comicViewHistoryRepository.save(viewHistory);
        }

        ComicViewHistoryService service = new ComicViewHistoryService(comicViewHistoryRepository, memberRepository);
        Page<ComicViewHistory> page = service.findAllByComicId(sampleComic.getId(), PageRequest.of(0, 10));

        assertThat(page.getTotalPages()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(10);

        ComicViewHistory firstViewHistory = page.getContent().get(0);
        assertThat(firstViewHistory.getId()).isNotNull();
        assertThat(firstViewHistory.getComic()).isNotNull();
        assertThat(firstViewHistory.getMember()).isNotNull();
        assertThat(firstViewHistory.getViewDateTime()).isNotNull();
    }

}