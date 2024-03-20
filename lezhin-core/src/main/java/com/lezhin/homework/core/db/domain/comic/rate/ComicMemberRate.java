package com.lezhin.homework.core.db.domain.comic.rate;

import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comic_member_rate", uniqueConstraints = {
        @UniqueConstraint(
                name = "unique_idx_comic_member_rate_1",
                columnNames = {"comic_id", "member_id"}
        )
})
public class ComicMemberRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            foreignKey = @ForeignKey(name = "fk_comic_member_rate_1"),
            nullable = false
    )
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "comic_id",
            foreignKey = @ForeignKey(name = "fk_comic_member_rate_2"),
            nullable = false
    )
    private Comic comic;

    @Column(nullable = false)
    private Boolean like;

    @Column
    private String comment;


}
