package com.sparta.trelloproject.domain.board.service;

import com.sparta.trelloproject.common.exception.NotFoundException;
import com.sparta.trelloproject.domain.board.entity.Board;
import com.sparta.trelloproject.domain.board.repository.BoardRepository;
import com.sparta.trelloproject.domain.workspace.entity.UserWorkspace;
import com.sparta.trelloproject.domain.workspace.repository.UserWorkSpaceRepository;
import com.sparta.trelloproject.domain.workspace.enums.WorkSpaceUserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private UserWorkSpaceRepository userWorkSpaceRepository;

    @InjectMocks
    private BoardService boardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("보드 삭제 성공")
    void deleteBoardSuccess() {
        Long boardId = 1L;
        Long userId = 1L;
        Long workspaceId = 2L;

        Board board = mock(Board.class);
        UserWorkspace userWorkspace = mock(UserWorkspace.class);

        when(userWorkspace.getWorkSpaceUserRole()).thenReturn(WorkSpaceUserRole.ROLE_WORKSPACE_ADMIN);

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        when(userWorkSpaceRepository.findByWorkspaceIdAndUserId(workspaceId, userId))
                .thenReturn(userWorkspace);

        doNothing().when(boardRepository).delete(board);

        boardService.deleteBoard(userId, boardId, workspaceId);

        verify(boardRepository).delete(board);
    }

    @Test
    @DisplayName("보드 삭제 실패 - 보드 없음")
    void deleteBoardNotFound() {
        Long boardId = 1L;
        Long userId = 1L;
        Long workspaceId = 2L;

        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            boardService.deleteBoard(userId, boardId, workspaceId);
        });
    }
}
