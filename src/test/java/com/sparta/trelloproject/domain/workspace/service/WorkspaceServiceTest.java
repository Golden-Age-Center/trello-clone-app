package com.sparta.trelloproject.domain.workspace.service;

import com.sparta.trelloproject.common.annotation.AuthUser;
import com.sparta.trelloproject.common.exception.ForbiddenException;
import com.sparta.trelloproject.common.exception.NotFoundException;
import com.sparta.trelloproject.common.exception.ResponseCode;
import com.sparta.trelloproject.domain.auth.dto.request.UserSignUpRequestDto;
import com.sparta.trelloproject.domain.notification.event.InvitedWorkspaceEvent;
import com.sparta.trelloproject.domain.user.entity.User;
import com.sparta.trelloproject.domain.user.enums.UserRole;
import com.sparta.trelloproject.domain.user.repository.UserRepository;
import com.sparta.trelloproject.domain.workspace.dto.WorkspaceInviteRequestDto;
import com.sparta.trelloproject.domain.workspace.dto.WorkspaceRequestDto;
import com.sparta.trelloproject.domain.workspace.dto.WorkspaceResponseDto;
import com.sparta.trelloproject.domain.workspace.entity.UserWorkspace;
import com.sparta.trelloproject.domain.workspace.entity.Workspace;
import com.sparta.trelloproject.domain.workspace.enums.WorkSpaceUserRole;
import com.sparta.trelloproject.domain.workspace.repository.UserWorkSpaceRepository;
import com.sparta.trelloproject.domain.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkspaceServiceTest {

    @Mock
    private WorkspaceRepository workspaceRepository;
    @Mock
    private UserWorkSpaceRepository userWorkSpaceRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private WorkspaceService workspaceService;

    private AuthUser authUser;

    @BeforeEach
    void setUp() {
        authUser = new AuthUser(1L , "test@test.com" , UserRole.ROLE_ADMIN);
    }

    @Test
    public void addWorkspace_동작완료() {
        // given
        UserSignUpRequestDto userSignUpRequestDto = new UserSignUpRequestDto("test@test.com","Aasdf1234!","테스트",UserRole.ROLE_ADMIN.name());
        User user = User.from("asdasdweqdasdas" , userSignUpRequestDto);

        WorkspaceRequestDto workspaceRequestDto = new WorkspaceRequestDto("워크스페이스 이름" , "워크스페이스 설명");
        Workspace workspace = Workspace.from(workspaceRequestDto.getWorkspaceName() , workspaceRequestDto.getWorkspaceDescription() , user);
        ReflectionTestUtils.setField(workspace, "id", 1L);
        UserWorkspace userWorkspace = UserWorkspace.from(workspace , user , WorkSpaceUserRole.ROLE_WORKSPACE_ADMIN);

        // when
        given(userRepository.findById(authUser.getUserId())).willReturn(Optional.of(user));
        given(workspaceRepository.save(any())).willReturn(workspace);
        given(userWorkSpaceRepository.save(any())).willReturn(userWorkspace);

        workspaceService.addWorkspace(authUser , workspaceRequestDto);

        // then
        assertEquals(workspace.getId() , userWorkspace.getWorkspace().getId());

        verify(workspaceRepository, times(1)).save(any(Workspace.class));
        verify(userWorkSpaceRepository , times(1)).save(any(UserWorkspace.class));
    }

    @Test
    public void addWorkspace_유저없음() {
        // given
        WorkspaceRequestDto workspaceRequestDto = new WorkspaceRequestDto("워크스페이스 이름" , "워크스페이스 설명");

        // when
        given(userRepository.findById(authUser.getUserId())).willReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> workspaceService.addWorkspace(authUser , workspaceRequestDto));

        // then
        assertEquals(exception.getMessage() , "해당 사용자는 존재하지 않습니다.");
    }

    @Test
    public void inviteMemberToWorkspace_동작완료() {
        // given
        Long userId = 1L;
        UserSignUpRequestDto userSignUpRequestDto = new UserSignUpRequestDto("test@test.com","Aasdf1234!","테스트",UserRole.ROLE_ADMIN.name());
        User user = User.from("asdasdweqdasdas" , userSignUpRequestDto);
        ReflectionTestUtils.setField(user, "id", userId);

        Long workspaceId = 1L;
        WorkspaceInviteRequestDto workspaceInviteRequestDto = new WorkspaceInviteRequestDto("test@test.com" , workspaceId , WorkSpaceUserRole.ROLE_WORKSPACE_ADMIN.name());

        Workspace workspace = Workspace.from("워크스페이스명" , "워크스페이스설명" , user);
        ReflectionTestUtils.setField(workspace, "id", workspaceId);

        UserWorkspace verificationUserWorkspace = UserWorkspace.from(workspace , user , WorkSpaceUserRole.ROLE_WORKSPACE_ADMIN);
        UserWorkspace userWorkspace = UserWorkspace.from(workspace , user , WorkSpaceUserRole.ROLE_WORKSPACE_ADMIN);

        // when
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        given(workspaceRepository.findById(anyLong())).willReturn(Optional.of(workspace));
        given(userWorkSpaceRepository.findByWorkspaceIdAndUserId(anyLong() , anyLong())).willReturn(verificationUserWorkspace);
        given(userWorkSpaceRepository.save(any())).willReturn(userWorkspace);
        doNothing().when(eventPublisher).publishEvent(any(InvitedWorkspaceEvent.class));

        workspaceService.inviteMemberToWorkspace(authUser , workspaceInviteRequestDto);

        // then
        verify(userWorkSpaceRepository , times(1)).save(any(UserWorkspace.class));
    }

    @Test
    public void inviteMemberToWorkspace_유저없음() {
        // given
        Long workspaceId = 1L;
        WorkspaceInviteRequestDto workspaceInviteRequestDto = new WorkspaceInviteRequestDto("test@test.com" , workspaceId , WorkSpaceUserRole.ROLE_WORKSPACE_ADMIN.name());

        // when
        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> workspaceService.inviteMemberToWorkspace(authUser , workspaceInviteRequestDto));

        // then
        assertEquals(exception.getMessage() , "해당 사용자는 존재하지 않습니다.");
    }

    @Test
    public void inviteMemberToWorkspace_워크스페이스없음() {
        // given
        Long userId = 1L;
        UserSignUpRequestDto userSignUpRequestDto = new UserSignUpRequestDto("test@test.com","Aasdf1234!","테스트",UserRole.ROLE_ADMIN.name());
        User user = User.from("asdasdweqdasdas" , userSignUpRequestDto);
        ReflectionTestUtils.setField(user, "id", userId);

        Long workspaceId = 1L;
        WorkspaceInviteRequestDto workspaceInviteRequestDto = new WorkspaceInviteRequestDto("test@test.com" , workspaceId , WorkSpaceUserRole.ROLE_WORKSPACE_ADMIN.name());


        // when
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        given(workspaceRepository.findById(anyLong())).willReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> workspaceService.inviteMemberToWorkspace(authUser , workspaceInviteRequestDto));

        // then
        assertEquals(exception.getMessage() , "해당 워크스페이스는 존재하지 않습니다.");
    }

    @Test
    public void inviteMemberToWorkspace_초대권한없음() {
        // given
        Long userId = 1L;
        UserSignUpRequestDto userSignUpRequestDto = new UserSignUpRequestDto("test@test.com","Aasdf1234!","테스트",UserRole.ROLE_ADMIN.name());
        User user = User.from("asdasdweqdasdas" , userSignUpRequestDto);
        ReflectionTestUtils.setField(user, "id", userId);

        Long workspaceId = 1L;
        WorkspaceInviteRequestDto workspaceInviteRequestDto = new WorkspaceInviteRequestDto("test@test.com" , workspaceId , WorkSpaceUserRole.ROLE_WORKSPACE_ADMIN.name());

        Workspace workspace = Workspace.from("워크스페이스명" , "워크스페이스설명" , user);
        ReflectionTestUtils.setField(workspace, "id", workspaceId);

        UserWorkspace verificationUserWorkspace = UserWorkspace.from(workspace , user , WorkSpaceUserRole.ROLE_EDIT_USER);

        // when
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        given(workspaceRepository.findById(anyLong())).willReturn(Optional.of(workspace));
        given(userWorkSpaceRepository.findByWorkspaceIdAndUserId(anyLong() , anyLong())).willReturn(verificationUserWorkspace);

        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> workspaceService.inviteMemberToWorkspace(authUser , workspaceInviteRequestDto));

        // then
        assertEquals(exception.getMessage() , "접근 권한이 없습니다.");
    }

    @Test
    public void getWorkspaces_동작완료() {
        // given
        List<Workspace> workspaces = List.of(
                Workspace.from("워크스페이스명1" , "설명1" , null),
                Workspace.from("워크스페이스명2" , "설명2" , null),
                Workspace.from("워크스페이스명3" , "설명3" , null)
        );

        // when
        given(workspaceRepository.getWorkspaces(anyLong())).willReturn(workspaces);

        List<WorkspaceResponseDto> workspaceResponseDtos = workspaceService.getWorkspaces(authUser);

        // then
        assertEquals(workspaceResponseDtos.size() , workspaces.size());
        assertEquals(workspaceResponseDtos.get(0).getWorkspaceName() , workspaces.get(0).getWorkspaceName());
    }

    @Test
    public void editWorkspace_동작완료() {
        // given
        Long userId = 1L;
        UserSignUpRequestDto userSignUpRequestDto = new UserSignUpRequestDto("test@test.com","Aasdf1234!","테스트",UserRole.ROLE_ADMIN.name());
        User user = User.from("asdasdweqdasdas" , userSignUpRequestDto);
        ReflectionTestUtils.setField(user, "id", userId);

        Long workspaceId = 1L;
        Workspace workspace = Workspace.from("워크스페이스명" , "워크스페이스설명" , user);
        ReflectionTestUtils.setField(workspace, "id", workspaceId);

        UserWorkspace userWorkspace = UserWorkspace.from(workspace , user , WorkSpaceUserRole.ROLE_WORKSPACE_ADMIN);

        WorkspaceRequestDto workspaceRequestDto = new WorkspaceRequestDto("워크스페이스명1" , "워크스페이스 설명1");

        // when
        given(workspaceRepository.findByIdAndUserId(anyLong() , anyLong())).willReturn(Optional.of(workspace));
        given(userWorkSpaceRepository.findByWorkspaceIdAndUserId(anyLong() , anyLong())).willReturn(userWorkspace);

        workspaceService.editWorkspace(authUser , workspaceId , workspaceRequestDto);
        workspace.update(workspaceRequestDto.getWorkspaceName() , workspaceRequestDto.getWorkspaceDescription());

        // then
        assertEquals(workspace.getWorkspaceName() , workspaceRequestDto.getWorkspaceName());
    }

    @Test
    public void editWorkspace_워크스페이스없음() {
        // given
        Long userId = 1L;
        UserSignUpRequestDto userSignUpRequestDto = new UserSignUpRequestDto("test@test.com","Aasdf1234!","테스트",UserRole.ROLE_ADMIN.name());
        User user = User.from("asdasdweqdasdas" , userSignUpRequestDto);
        ReflectionTestUtils.setField(user, "id", userId);

        Long workspaceId = 1L;

        WorkspaceRequestDto workspaceRequestDto = new WorkspaceRequestDto("워크스페이스명1" , "워크스페이스 설명1");

        // when
        given(workspaceRepository.findByIdAndUserId(anyLong() , anyLong())).willReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> workspaceService.editWorkspace(authUser , workspaceId , workspaceRequestDto));

        // then
        assertEquals(exception.getMessage() , "해당 워크스페이스는 존재하지 않습니다.");
    }

    @Test
    public void editWorkspace_수정권한없음() {
        // given
        Long userId = 1L;
        UserSignUpRequestDto userSignUpRequestDto = new UserSignUpRequestDto("test@test.com","Aasdf1234!","테스트",UserRole.ROLE_ADMIN.name());
        User user = User.from("asdasdweqdasdas" , userSignUpRequestDto);
        ReflectionTestUtils.setField(user, "id", userId);

        Long workspaceId = 1L;
        Workspace workspace = Workspace.from("워크스페이스명" , "워크스페이스설명" , user);
        ReflectionTestUtils.setField(workspace, "id", workspaceId);

        UserWorkspace userWorkspace = UserWorkspace.from(workspace , user , WorkSpaceUserRole.ROLE_EDIT_USER);

        WorkspaceRequestDto workspaceRequestDto = new WorkspaceRequestDto("워크스페이스명1" , "워크스페이스 설명1");

        // when
        given(workspaceRepository.findByIdAndUserId(anyLong() , anyLong())).willReturn(Optional.of(workspace));
        given(userWorkSpaceRepository.findByWorkspaceIdAndUserId(anyLong() , anyLong())).willReturn(userWorkspace);

        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> workspaceService.editWorkspace(authUser , workspaceId , workspaceRequestDto));

        // then
        assertEquals(exception.getMessage() , "접근 권한이 없습니다.");
    }

    @Test
    public void removeWorkspace_동작완료() {
        // given
        Long userId = 1L;
        UserSignUpRequestDto userSignUpRequestDto = new UserSignUpRequestDto("test@test.com","Aasdf1234!","테스트",UserRole.ROLE_ADMIN.name());
        User user = User.from("asdasdweqdasdas" , userSignUpRequestDto);
        ReflectionTestUtils.setField(user, "id", userId);

        Long workspaceId = 1L;
        Workspace workspace = Workspace.from("워크스페이스명" , "워크스페이스설명" , user);
        ReflectionTestUtils.setField(workspace, "id", workspaceId);

        UserWorkspace userWorkspace = UserWorkspace.from(workspace , user , WorkSpaceUserRole.ROLE_WORKSPACE_ADMIN);

        // when
        given(workspaceRepository.findByIdAndUserId(anyLong() , anyLong())).willReturn(Optional.of(workspace));
        given(userWorkSpaceRepository.findByWorkspaceIdAndUserId(anyLong() , anyLong())).willReturn(userWorkspace);
        doNothing().when(workspaceRepository).delete(any());

        workspaceService.removeWorkspace(authUser , workspaceId);

        // then
        verify(workspaceRepository, times(1)).delete(any());
    }

    @Test
    public void removeWorkspace_워크스페이스없음() {
        // given
        Long userId = 1L;
        UserSignUpRequestDto userSignUpRequestDto = new UserSignUpRequestDto("test@test.com","Aasdf1234!","테스트",UserRole.ROLE_ADMIN.name());
        User user = User.from("asdasdweqdasdas" , userSignUpRequestDto);
        ReflectionTestUtils.setField(user, "id", userId);

        Long workspaceId = 1L;

        // when
        given(workspaceRepository.findByIdAndUserId(anyLong() , anyLong())).willReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> workspaceService.removeWorkspace(authUser , workspaceId));

        // then
        assertEquals(exception.getMessage() , "해당 워크스페이스는 존재하지 않습니다.");
    }

    @Test
    public void removeWorkspace_삭제권한없음() {
        // given
        Long userId = 1L;
        UserSignUpRequestDto userSignUpRequestDto = new UserSignUpRequestDto("test@test.com","Aasdf1234!","테스트",UserRole.ROLE_ADMIN.name());
        User user = User.from("asdasdweqdasdas" , userSignUpRequestDto);
        ReflectionTestUtils.setField(user, "id", userId);

        Long workspaceId = 1L;
        Workspace workspace = Workspace.from("워크스페이스명" , "워크스페이스설명" , user);
        ReflectionTestUtils.setField(workspace, "id", workspaceId);

        UserWorkspace userWorkspace = UserWorkspace.from(workspace , user , WorkSpaceUserRole.ROLE_EDIT_USER);

        // when
        given(workspaceRepository.findByIdAndUserId(anyLong() , anyLong())).willReturn(Optional.of(workspace));
        given(userWorkSpaceRepository.findByWorkspaceIdAndUserId(anyLong() , anyLong())).willReturn(userWorkspace);

        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> workspaceService.removeWorkspace(authUser , workspaceId));

        // then
        assertEquals(exception.getMessage() , "접근 권한이 없습니다.");
    }

}