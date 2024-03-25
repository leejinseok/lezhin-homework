package com.lezhin.homework.api;

import com.lezhin.homework.api.application.domain.auth.AuthService;
import com.lezhin.homework.api.application.domain.author.AuthorService;
import com.lezhin.homework.api.application.domain.comic.ComicMemberRateFacade;
import com.lezhin.homework.api.application.domain.comic.ComicService;
import com.lezhin.homework.api.presentation.auth.dto.SignUpRequest;
import com.lezhin.homework.api.presentation.author.dto.AuthorRequest;
import com.lezhin.homework.api.presentation.comic.dto.ComicMemberRateRequest;
import com.lezhin.homework.api.presentation.comic.dto.ComicRequest;
import com.lezhin.homework.core.db.domain.Gender;
import com.lezhin.homework.core.db.domain.author.Author;
import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.comic.ComicType;
import com.lezhin.homework.core.db.domain.member.Member;
import com.lezhin.homework.core.db.domain.member.MemberType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class ApiApplicationRunner implements ApplicationRunner {

    private final ComicMemberRateFacade comicMemberRateFacade;
    private final ComicService comicService;
    private final AuthorService authorService;
    private final AuthService authService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Author author = saveAuthor();
        List<Comic> comics = saveComics(author);
        List<Member> members = saveMembers();
        saveComicMemberRates(members, comics);
    }

    private void saveComicMemberRates(final List<Member> members, final List<Comic> comics) {
        for (Member member : members) {
            for (Comic comic : comics) {
                boolean randomLike = new Random().nextBoolean();
                ComicMemberRateRequest request = ComicMemberRateRequest.of(
                        comic.getId(),
                        randomLike,
                        "하하하"
                );


                try {
                    comicMemberRateFacade.rateComicWithLock(member.getId(), request);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private List<Member> saveMembers() {
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            SignUpRequest signUpRequest = SignUpRequest.of(
                    "사용자명" + i,
                    "email" + i + "@lezhin.com",
                    "password",
                    Gender.MALE,
                    MemberType.NORMAL
            );
            Member member = authService.signUp(signUpRequest);
            members.add(member);
        }

        return members;
    }

    private Author saveAuthor() {
        return authorService.save(AuthorRequest.of("박작가"));
    }

    private List<Comic> saveComics(final Author author) {
        List<Comic> comics = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ComicRequest comicRequest = ComicRequest.of(
                    "만화명 " + i,
                    author.getId(),
                    ComicType.NORMAL,
                    new BigDecimal(0),
                    LocalDate.now(),
                    0,
                    0
            );
            Comic save = comicService.save(comicRequest);
            comics.add(save);
        }

        return comics;
    }

}
