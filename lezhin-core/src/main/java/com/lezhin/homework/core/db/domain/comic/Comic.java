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

    @Column
    private LocalDate openDate;

    @Column
    private Long likes;

    @Column
    private Long dislikes;


}
