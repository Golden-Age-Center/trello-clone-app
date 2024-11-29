package com.sparta.trelloproject.domain.workspace.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.trelloproject.domain.workspace.entity.QUserWorkspace;
import com.sparta.trelloproject.domain.workspace.entity.QWorkspace;
import com.sparta.trelloproject.domain.workspace.entity.Workspace;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class WorkspaceQueryRepositoryImpl implements WorkspaceQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Workspace> getWorkspaces(Long userId) {
        QWorkspace w = QWorkspace.workspace;
        QUserWorkspace uw = QUserWorkspace.userWorkspace;

        return jpaQueryFactory
                .select(w)
                .from(uw)
                .innerJoin(w).on(w.id.eq(uw.workspace.id))
                .where(uw.user.id.eq(userId))
                .fetch();
    }
}
