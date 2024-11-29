package com.sparta.trelloproject.data.list;

import com.sparta.trelloproject.data.board.BoardMockDataUtil;
import com.sparta.trelloproject.domain.list.dto.ListAuthorityDto;
import com.sparta.trelloproject.domain.list.dto.request.ListCreateRequestDto;
import com.sparta.trelloproject.domain.list.dto.request.ListDeleteRequestDto;
import com.sparta.trelloproject.domain.list.dto.request.ListUpdateRequestDto;
import com.sparta.trelloproject.domain.list.entity.Lists;
import com.sparta.trelloproject.domain.workspace.enums.WorkSpaceUserRole;
import org.springframework.test.util.ReflectionTestUtils;

import static com.sparta.trelloproject.data.board.BoardMockDataUtil.mockBoard;

public class ListMockDataUtil {
    public static Lists mockLists() {
        Lists lists = Lists.of("title", 5, mockBoard());
        ReflectionTestUtils.setField(lists, "id", 1L);

        return lists;
    }

    public static ListCreateRequestDto mockListCreateRequestDto() {
        return new ListCreateRequestDto("title", 1L, 1L);
    }

    public static ListUpdateRequestDto mockListUpdateRequestDto() {
        return new ListUpdateRequestDto("updateTitle", 1L);
    }

    public static ListDeleteRequestDto mockListDeleteRequestDto() {
        return new ListDeleteRequestDto(1L);
    }

    public static ListAuthorityDto mockListAuthorityDto_READ() {
        return new ListAuthorityDto(1, WorkSpaceUserRole.ROLE_READ_USER, 1L, 1, "title", 10);
    }

    public static ListAuthorityDto mockListAuthorityDto_EDIT() {
        return new ListAuthorityDto(1, WorkSpaceUserRole.ROLE_EDIT_USER, 1L, 1, "title", 10);
    }

    public static ListAuthorityDto mockListAuthorityDto_Sequence(int sequence) {
        return new ListAuthorityDto(1, WorkSpaceUserRole.ROLE_EDIT_USER, 1L, sequence, "title", 10);
    }
}
