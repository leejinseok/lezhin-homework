package com.lezhin.homework.api;

import com.lezhin.homework.core.db.domain.comic.ComicRepository;
import com.lezhin.homework.core.db.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiApplicationRunner implements ApplicationRunner {

    private final MemberRepository memberRepository;
    private final ComicRepository comicRepository;


    @Override
    public void run(ApplicationArguments args) throws Exception {

    }

}
