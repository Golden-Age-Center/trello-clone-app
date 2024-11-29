package com.sparta.trelloproject.domain.board.service;

import com.sparta.trelloproject.common.exception.ForbiddenException;
import com.sparta.trelloproject.common.exception.NotFoundException;
import com.sparta.trelloproject.common.exception.ResponseCode;
import com.sparta.trelloproject.domain.board.dto.request.BoardRequestDto;
import com.sparta.trelloproject.domain.board.dto.response.BoardCreateResponseDto;
import com.sparta.trelloproject.domain.board.dto.response.BoardDetailResponseDto;
import com.sparta.trelloproject.domain.board.dto.response.BoardUpdateResponseDto;
import com.sparta.trelloproject.domain.board.entity.Board;
import com.sparta.trelloproject.domain.board.repository.BoardQueryRepository;
import com.sparta.trelloproject.domain.board.repository.BoardRepository;
import com.sparta.trelloproject.domain.list.dto.response.ListResponseDto;
import com.sparta.trelloproject.domain.workspace.entity.UserWorkspace;
import com.sparta.trelloproject.domain.workspace.enums.WorkSpaceUserRole;
import com.sparta.trelloproject.domain.workspace.repository.UserWorkSpaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardQueryRepository boardQueryRepository;
    private final UserWorkSpaceRepository userWorkSpaceRepository;

    // 보드 생성
    public BoardCreateResponseDto createBoard(Long userId, BoardRequestDto boardRequestDto) {
        UserWorkspace userWorkspace = userWorkSpaceRepository.findByWorkspaceIdAndUserId(boardRequestDto.getWorkspaceId(), userId);

        if (userWorkspace == null || !isEditorOrAdmin(userWorkspace)) {
            throw new ForbiddenException(ResponseCode.FORBIDDEN);
        }

        Board board = Board.of(boardRequestDto.getTitle(), boardRequestDto.getBackgroundColor(), userWorkspace.getWorkspace());
        boardRepository.save(board);
        return BoardCreateResponseDto.of(board);
    }

    // 보드 수정
    public BoardUpdateResponseDto updateBoard(Long userId, Long boardId, BoardRequestDto boardRequestDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_BOARD));

        if (!board.getWorkspace().getId().equals(boardRequestDto.getWorkspaceId())) {
            throw new ForbiddenException(ResponseCode.FORBIDDEN);
        }

        UserWorkspace userWorkspace = userWorkSpaceRepository.findByWorkspaceIdAndUserId(board.getWorkspace().getId(), userId);

        if (userWorkspace == null || !isEditorOrAdmin(userWorkspace)) {
            throw new ForbiddenException(ResponseCode.FORBIDDEN);
        }

        board.update(boardRequestDto.getTitle(), boardRequestDto.getBackgroundColor());
        return BoardUpdateResponseDto.of(board);
    }

    // 보드 조회(다건)
    @Transactional(readOnly = true)
    public List<BoardDetailResponseDto> getBoardList(Long userId, Long workspaceId) {
        UserWorkspace userWorkspace = userWorkSpaceRepository.findByWorkspaceIdAndUserId(workspaceId, userId);

        if (userWorkspace == null) {
            throw new ForbiddenException(ResponseCode.FORBIDDEN);
        }

        List<Board> boards = boardRepository.findAllByWorkspaceId(workspaceId);

        return boards.stream()
                .map(board -> BoardDetailResponseDto.from(board, null))
                .collect(Collectors.toList());
    }

    // 보드 조회(단건)
    @Transactional(readOnly = true)
    public BoardDetailResponseDto getBoard(Long userId, Long boardId) {
        Board board = boardQueryRepository.findByIdWithDetails(boardId)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_BOARD));

        UserWorkspace userWorkspace = userWorkSpaceRepository.findByWorkspaceIdAndUserId(board.getWorkspace().getId(), userId);

        if (userWorkspace == null) {
            throw new ForbiddenException(ResponseCode.FORBIDDEN);
        }

        List<ListResponseDto> listResponseDtos = board.getLists().stream()
                .map(ListResponseDto::from)
                .collect(Collectors.toList());

        return BoardDetailResponseDto.from(board, listResponseDtos);
    }

    // 보드 삭제
    public void deleteBoard(Long userId, Long boardId, Long workspaceId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_BOARD));

        UserWorkspace userWorkspace = userWorkSpaceRepository.findByWorkspaceIdAndUserId(workspaceId, userId);

        if (userWorkspace == null || !isEditorOrAdmin(userWorkspace)) {
            throw new ForbiddenException(ResponseCode.FORBIDDEN);
        }

        boardRepository.delete(board);
    }

    // 권한 확인
    private boolean isEditorOrAdmin(UserWorkspace userWorkspace) {
        return userWorkspace.getWorkSpaceUserRole() == WorkSpaceUserRole.ROLE_WORKSPACE_ADMIN ||
                userWorkspace.getWorkSpaceUserRole() == WorkSpaceUserRole.ROLE_EDIT_USER;
    }
}
