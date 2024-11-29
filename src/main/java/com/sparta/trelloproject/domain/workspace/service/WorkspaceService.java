package com.sparta.trelloproject.domain.workspace.service;

import com.sparta.trelloproject.common.annotation.AuthUser;
import com.sparta.trelloproject.common.exception.ForbiddenException;
import com.sparta.trelloproject.common.exception.NotFoundException;
import com.sparta.trelloproject.common.exception.ResponseCode;
import com.sparta.trelloproject.domain.notification.event.InvitedWorkspaceEvent;
import com.sparta.trelloproject.domain.notification.event.SavedCommentEvent;
import com.sparta.trelloproject.domain.user.entity.User;
import com.sparta.trelloproject.domain.user.repository.UserRepository;
import com.sparta.trelloproject.domain.workspace.dto.WorkspaceInviteRequestDto;
import com.sparta.trelloproject.domain.workspace.dto.WorkspaceRequestDto;
import com.sparta.trelloproject.domain.workspace.dto.WorkspaceResponseDto;
import com.sparta.trelloproject.domain.workspace.entity.UserWorkspace;
import com.sparta.trelloproject.domain.workspace.entity.Workspace;
import com.sparta.trelloproject.domain.workspace.enums.WorkSpaceUserRole;
import com.sparta.trelloproject.domain.workspace.repository.UserWorkSpaceRepository;
import com.sparta.trelloproject.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserWorkSpaceRepository userWorkSpaceRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void addWorkspace(AuthUser authUser , WorkspaceRequestDto workSpaceRequestDto) {
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_USER));

        Workspace workspace = Workspace.from(workSpaceRequestDto.getWorkspaceName() , workSpaceRequestDto.getWorkspaceDescription() , user);
        UserWorkspace userWorkspace = UserWorkspace.from(workspace , user , WorkSpaceUserRole.ROLE_WORKSPACE_ADMIN);

        workspaceRepository.save(workspace);
        userWorkSpaceRepository.save(userWorkspace);
    }

    @Transactional
    public void inviteMemberToWorkspace(AuthUser authUser , WorkspaceInviteRequestDto workspaceInviteRequestDto) {
        User user = userRepository.findByEmail(workspaceInviteRequestDto.getEmail()).orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_USER));
        Workspace workspace = workspaceRepository.findById(workspaceInviteRequestDto.getWorkspaceId()).orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_WORKSPACE));

        UserWorkspace veriUserWorkspace = userWorkSpaceRepository.findByWorkspaceIdAndUserId(workspaceInviteRequestDto.getWorkspaceId() , authUser.getUserId());
        if (veriUserWorkspace.getWorkSpaceUserRole() != WorkSpaceUserRole.ROLE_WORKSPACE_ADMIN) {
            throw new ForbiddenException(ResponseCode.FORBIDDEN);
        }

        UserWorkspace userWorkspace = UserWorkspace.from(workspace , user , WorkSpaceUserRole.of(workspaceInviteRequestDto.getWorkspaceUserRole()));

        userWorkSpaceRepository.save(userWorkspace);

        /**
         * 워크스페이스 초대 알림 전송 이벤트 발생
         */
        eventPublisher.publishEvent(new InvitedWorkspaceEvent(user.getId()));
    }

    public List<WorkspaceResponseDto> getWorkspaces(AuthUser authUser) {
        return workspaceRepository.getWorkspaces(authUser.getUserId()).stream()
                .map(it -> new WorkspaceResponseDto(it.getId() , it.getWorkspaceName() , it.getWorkspaceDescription()))
                .toList();
    }

    @Transactional
    public void editWorkspace(AuthUser authUser , Long workspaceId , WorkspaceRequestDto workSpaceRequestDto) {
        Workspace workspace = workspaceRepository.findByIdAndUserId(workspaceId , authUser.getUserId()).orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_WORKSPACE));
        UserWorkspace userWorkspace = userWorkSpaceRepository.findByWorkspaceIdAndUserId(workspaceId , authUser.getUserId());
        if (userWorkspace.getWorkSpaceUserRole() != WorkSpaceUserRole.ROLE_WORKSPACE_ADMIN) {
            throw new ForbiddenException(ResponseCode.FORBIDDEN);
        }

        workspace.update(workSpaceRequestDto.getWorkspaceName() , workSpaceRequestDto.getWorkspaceDescription());
    }

    @Transactional
    public void removeWorkspace(AuthUser authUser ,Long workspaceId) {
        Workspace workspace = workspaceRepository.findByIdAndUserId(workspaceId , authUser.getUserId()).orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_WORKSPACE));
        UserWorkspace userWorkspace = userWorkSpaceRepository.findByWorkspaceIdAndUserId(workspaceId , authUser.getUserId());
        if (userWorkspace.getWorkSpaceUserRole() != WorkSpaceUserRole.ROLE_WORKSPACE_ADMIN) {
            throw new ForbiddenException(ResponseCode.FORBIDDEN);
        }

        // TODO : 삭제시 워크스페이스 내의 모든 보드와 데이터가 삭제되는 건 나중으로 cascade 로 사용할 것
        workspaceRepository.delete(workspace);
    }
}
