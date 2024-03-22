package com.lezhin.homework.core.db.domain.comic.view;

import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comic_search_history")
public class ComicViewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            foreignKey = @ForeignKey(name = "fk_comic_search_history_1"),
            nullable = false
    )
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "comic_id",
            foreignKey = @ForeignKey(name = "fk_comic_search_history_2"),
            nullable = false
    )
    private Comic comic;

    @Column
    private LocalDateTime viewDateTime;

    public static ComicViewHistory create(final Member member, final Comic comic, final LocalDateTime viewDateTime) {
        ComicViewHistory viewHistory = new ComicViewHistory();
        viewHistory.member = member;
        viewHistory.comic = comic;
        viewHistory.viewDateTime = viewDateTime;
        return viewHistory;
    }

}
