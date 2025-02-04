package com.lezhin.homework.core.db.domain.comic;

import com.lezhin.homework.core.db.domain.author.Author;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comic")
public class Comic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String contentsName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    private Author author;

    @Column
    private BigDecimal coin;

    @Enumerated(EnumType.STRING)
    private ComicType comicType;

    @Column
    private LocalDate openDate;

    @Column
    private long likes;

    @Column
    private long dislikes;

    public static Comic of(
            final String contentsName,
            final BigDecimal coin,
            final ComicType type,
            final LocalDate openDate,
            final long likes,
            final long dislikes
    ) {
        return Comic.builder()
                .contentsName(contentsName)
                .coin(coin)
                .comicType(type)
                .openDate(openDate)
                .likes(likes)
                .dislikes(dislikes)
                .build();
    }

    public void updateLikesAndDislikes(final long likes, final long dislikes) {
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public void updateCoin(final BigDecimal coin) {
        this.coin = coin;
    }


}
