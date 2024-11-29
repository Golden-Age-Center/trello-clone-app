package com.sparta.trelloproject.domain.board.entity;

import com.sparta.trelloproject.common.entity.Timestamped;
import com.sparta.trelloproject.domain.list.entity.Lists;
import com.sparta.trelloproject.domain.workspace.entity.Workspace;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Set;

@Entity
@Table(name = "boards")
@Getter
@NoArgsConstructor
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String backgroundColor;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "workspaces_id", nullable = false)
    private Workspace workspace;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Lists> lists;

    public static Board of(String title, String backgroundColor, Workspace workspace) {
        Board board = new Board();
        board.title = title;
        board.backgroundColor = backgroundColor;
        board.workspace = workspace;
        return board;
    }

    public void update(String title, String backgroundColor) {
        this.title = title;
        this.backgroundColor = backgroundColor;
    }
}
