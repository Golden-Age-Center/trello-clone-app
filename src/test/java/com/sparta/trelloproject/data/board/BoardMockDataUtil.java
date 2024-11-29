package com.sparta.trelloproject.data.board;

import com.sparta.trelloproject.domain.board.dto.BoardAuthorityDto;
import com.sparta.trelloproject.domain.board.entity.Board;
import com.sparta.trelloproject.domain.workspace.enums.WorkSpaceUserRole;
import org.springframework.test.util.ReflectionTestUtils;

import static com.sparta.trelloproject.data.workspace.WorkspaceMockDataUtil.mockWorkspace;

public class BoardMockDataUtil {
    public static BoardAuthorityDto mockBoardAuthorityDto_READ() {
        return new BoardAuthorityDto(1L, "title", "background", 1, 1, WorkSpaceUserRole.ROLE_READ_USER);
    }

    public static BoardAuthorityDto mockBoardAuthorityDto_EDIT() {
        return new BoardAuthorityDto(1L, "title", "background", 1, 1, WorkSpaceUserRole.ROLE_EDIT_USER);
    }

    public static Board mockBoard() {
        Board board = Board.of("title", "backgroundColor", mockWorkspace());
        ReflectionTestUtils.setField(board, "id", 1L);

        return board;
    }
}
