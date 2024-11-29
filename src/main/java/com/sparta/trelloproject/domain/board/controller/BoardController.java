package com.sparta.trelloproject.domain.board.controller;

import com.sparta.trelloproject.common.annotation.AuthUser;
import com.sparta.trelloproject.common.response.SuccessResponse;
import com.sparta.trelloproject.domain.board.dto.request.BoardDeleteRequestDto;
import com.sparta.trelloproject.domain.board.dto.request.BoardRequestDto;
import com.sparta.trelloproject.domain.board.dto.response.BoardCreateResponseDto;
import com.sparta.trelloproject.domain.board.dto.response.BoardDetailResponseDto;
import com.sparta.trelloproject.domain.board.dto.response.BoardUpdateResponseDto;
import com.sparta.trelloproject.domain.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 보드 생성
    @PostMapping("/v1/boards")
    public ResponseEntity<SuccessResponse<BoardCreateResponseDto>> createBoard(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody BoardRequestDto boardRequestDto) {
        BoardCreateResponseDto createdBoard = boardService.createBoard(authUser.getUserId(), boardRequestDto);
        return ResponseEntity.ok(SuccessResponse.of(createdBoard));
    }

    // 보드 수정
    @PatchMapping("/v1/boards/{boardId}")
    public ResponseEntity<SuccessResponse<BoardUpdateResponseDto>> updateBoard(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long boardId,
            @Valid @RequestBody BoardRequestDto boardRequestDto) {
        BoardUpdateResponseDto updatedBoard = boardService.updateBoard(authUser.getUserId(), boardId, boardRequestDto);
        return ResponseEntity.ok(SuccessResponse.of(updatedBoard));
    }

    // 보드 다건 조회
    @GetMapping("/v1/boards")
    public ResponseEntity<SuccessResponse<List<BoardDetailResponseDto>>> getBoardList(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam Long workspaceId) {
        List<BoardDetailResponseDto> boards = boardService.getBoardList(authUser.getUserId(), workspaceId);
        return ResponseEntity.ok(SuccessResponse.of(boards));
    }

    // 보드 단건 조회
    @GetMapping("/v1/boards/{boardId}")
    public ResponseEntity<SuccessResponse<BoardDetailResponseDto>> getBoard(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long boardId) {
        BoardDetailResponseDto boardResponseDto = boardService.getBoard(authUser.getUserId(), boardId);
        return ResponseEntity.ok(SuccessResponse.of(boardResponseDto));
    }

    // 보드 삭제
    @DeleteMapping("/v1/boards/{boardId}")
    public ResponseEntity<SuccessResponse<Void>> deleteBoard(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long boardId,
            @Valid @RequestBody BoardDeleteRequestDto boardDeleteRequestDto) {
        boardService.deleteBoard(authUser.getUserId(), boardId, boardDeleteRequestDto.getWorkspaceId());
        return ResponseEntity.ok(SuccessResponse.of(null));
    }
}
