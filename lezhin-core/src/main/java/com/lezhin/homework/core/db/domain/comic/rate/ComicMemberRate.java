package com.lezhin.homework.core.db.domain.comic.rate;

import com.lezhin.homework.core.db.domain.comic.Comic;
import com.lezhin.homework.core.db.domain.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

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
@EntityListeners(AuditingEntityListener.class)
public class ComicMemberRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "comic_id",
            foreignKey = @ForeignKey(name = "fk_comic_member_rate_1"),
            nullable = false
    )
    private Comic comic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            foreignKey = @ForeignKey(name = "fk_comic_member_rate_2"),
            nullable = false
    )
    private Member member;

    @Column(nullable = false)
    private Boolean isLike;

    @Column
    private String comment;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime registerDateTime;

    public static ComicMemberRate of(final boolean isLike, final String comment) {
        ComicMemberRate rate = new ComicMemberRate();
        rate.isLike = isLike;
        rate.comment = comment;
        return rate;
    }

    public void setMember(final Member member) {
        this.member = member;
        member.getComicMemberRates().add(this);
    }

    public void setComic(final Comic comic) {
        this.comic = comic;
    }
}
