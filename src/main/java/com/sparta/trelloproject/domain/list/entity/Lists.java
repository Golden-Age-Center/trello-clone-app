package com.sparta.trelloproject.domain.list.entity;

import com.sparta.trelloproject.domain.board.entity.Board;
import com.sparta.trelloproject.domain.card.entity.Card;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lists")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lists {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    @Column
    private Integer sequence;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boards_id")
    private Board board;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards;

    private Lists(String title, Integer sequence, Board board) {
        this.title = title;
        this.sequence = sequence;
        this.board = board;
        this.cards = new ArrayList<>();
    }

    public List<Card> getCards() {
        return cards;
    }

    public static Lists of(String title, int sequence, Board board) {
        return new Lists(
                title,
                sequence,
                board
        );
    }

    public void updateListTitle(String title) {
        this.title = title;
    }
}
