package com.sparta.trelloproject.domain.workspace.entity;

import com.sparta.trelloproject.domain.user.entity.User;
import com.sparta.trelloproject.domain.workspace.enums.WorkSpaceUserRole;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name = "user_workspace")
@Getter
public class UserWorkspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    private WorkSpaceUserRole workSpaceUserRole;

    private UserWorkspace(Workspace workspace, User user , WorkSpaceUserRole workSpaceUserRole) {
        this.workspace = workspace;
        this.user = user;
        this.workSpaceUserRole = workSpaceUserRole;
    }

    public static UserWorkspace from(Workspace workspace, User user , WorkSpaceUserRole workSpaceUserRole) {
        return new UserWorkspace(workspace , user , workSpaceUserRole);
    }
}
