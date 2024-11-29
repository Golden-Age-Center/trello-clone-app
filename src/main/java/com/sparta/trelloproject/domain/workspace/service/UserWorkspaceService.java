package com.sparta.trelloproject.domain.workspace.service;

import com.sparta.trelloproject.domain.workspace.repository.UserWorkSpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserWorkspaceService {
    private final UserWorkSpaceRepository userWorkSpaceRepository;

    public void deleteUserWorkspace(long userId) {
        userWorkSpaceRepository.deleteByUserId(userId);
    }
}
