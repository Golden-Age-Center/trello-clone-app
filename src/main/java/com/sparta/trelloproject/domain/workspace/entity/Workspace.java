package com.sparta.trelloproject.domain.workspace.entity;

import com.sparta.trelloproject.common.entity.Timestamped;
import com.sparta.trelloproject.domain.board.entity.Board;
import com.sparta.trelloproject.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@NoArgsConstructor
@Entity
@Table(name = "workspaces")
@Getter
public class Workspace extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String workspaceName;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private String workspaceDescription;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "workspace" , orphanRemoval = true)
    private List<UserWorkspace> userWorkspaces;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "workspace" , orphanRemoval = true)
    private List<Board> boards;

    private Workspace(String workspaceName, String workspaceDescription , User user) {
        this.workspaceName = workspaceName;
        this.workspaceDescription = workspaceDescription;
        this.user = user;
    }

    public static Workspace from(String workspaceName , String workspaceDescription , User user) {
        return new Workspace(workspaceName , workspaceDescription , user);
    }

    public void update(String workspaceName , String workspaceDescription) {
        this.workspaceName = workspaceName;
        this.workspaceDescription = workspaceDescription;
    }
}
