package com.lezhin.homework.core.db.domain.member;

import com.lezhin.homework.core.db.domain.Gender;
import com.lezhin.homework.core.db.domain.comic.rate.ComicMemberRate;
import com.lezhin.homework.core.db.domain.comic.view.ComicViewHistory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member")
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column
    private MemberType memberType;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime registerDateTime;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = {CascadeType.ALL})
    private List<ComicMemberRate> comicMemberRates = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = {CascadeType.ALL})
    private List<ComicViewHistory> comicViewHistories = new ArrayList<>();

    public static Member create(
            final String username,
            final String userEmail,
            final String password,
            final Gender gender,
            final MemberType type,
            final LocalDateTime registerDateTime
    ) {
        return Member.builder()
                .userName(username)
                .userEmail(userEmail)
                .password(password)
                .gender(gender)
                .memberType(type)
                .registerDateTime(registerDateTime)
                .build();
    }


}
