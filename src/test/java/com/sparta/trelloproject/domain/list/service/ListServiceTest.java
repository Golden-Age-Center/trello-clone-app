package com.sparta.trelloproject.domain.list.service;

import com.sparta.trelloproject.common.exception.ForbiddenException;
import com.sparta.trelloproject.common.exception.InvalidParameterException;
import com.sparta.trelloproject.common.exception.NotFoundException;
import com.sparta.trelloproject.data.board.BoardMockDataUtil;
import com.sparta.trelloproject.domain.board.dto.BoardAuthorityDto;
import com.sparta.trelloproject.domain.board.entity.Board;
import com.sparta.trelloproject.domain.board.repository.BoardRepository;
import com.sparta.trelloproject.domain.list.dto.ListAuthorityDto;
import com.sparta.trelloproject.domain.list.dto.request.ListCreateRequestDto;
import com.sparta.trelloproject.domain.list.dto.request.ListDeleteRequestDto;
import com.sparta.trelloproject.domain.list.dto.request.ListUpdateRequestDto;
import com.sparta.trelloproject.domain.list.dto.response.ListUpdateResponseDto;
import com.sparta.trelloproject.domain.list.entity.Lists;
import com.sparta.trelloproject.domain.list.repository.ListRepository;
import com.sparta.trelloproject.domain.workspace.repository.UserWorkSpaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.sparta.trelloproject.common.exception.ResponseCode.FORBIDDEN;
import static com.sparta.trelloproject.common.exception.ResponseCode.NOT_FOUND_LIST;
import static com.sparta.trelloproject.data.board.BoardMockDataUtil.mockBoard;
import static com.sparta.trelloproject.data.list.ListMockDataUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ListServiceTest {
    @InjectMocks
    private ListService listService;

    @Mock
    private UserWorkSpaceRepository userWorkSpaceRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private ListRepository listRepository;

    @Nested
    @DisplayName("리스트 생성 테스트 케이스")
    class CreateListTest {
        @Test
        @DisplayName("잘못된 보드 고유번호/사용자 고유번호/워크스페이스 고유번호로 인해 리스트 생성 실패")
        public void createList_invalidId_failure() {
            // given
            long userId = 1;

            ListCreateRequestDto listCreateRequestDto = mockListCreateRequestDto();

            given(userWorkSpaceRepository.findByBoardId(anyLong(), anyLong(), anyLong()))
                    .willThrow(new NotFoundException(NOT_FOUND_LIST));

            // when
            Throwable t = assertThrows(NotFoundException.class,
                    () -> listService.createList(userId, listCreateRequestDto));

            // then
            assertEquals(NOT_FOUND_LIST.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("잘못된 워크스페이스 권한으로 인해 리스트 생성 실패")
        public void createList_forbiddenAuthority_failure() {
            // given
            long userId = 1;

            ListCreateRequestDto listCreateRequestDto = mockListCreateRequestDto();

            BoardAuthorityDto boardAuthorityDto = BoardMockDataUtil.mockBoardAuthorityDto_READ();

            given(userWorkSpaceRepository.findByBoardId(anyLong(), anyLong(), anyLong())).willReturn(boardAuthorityDto);

            // when
            Throwable t = assertThrows(ForbiddenException.class,
                    () -> listService.createList(userId, listCreateRequestDto));

            // then
            assertEquals(FORBIDDEN.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("리스트 생성 성공")
        public void createList_success() {
            // given
            long userId = 1;

            ListCreateRequestDto listCreateRequestDto = mockListCreateRequestDto();

            BoardAuthorityDto boardAuthorityDto = BoardMockDataUtil.mockBoardAuthorityDto_EDIT();
            Board board = mockBoard();
            Lists lists = mockLists();

            given(userWorkSpaceRepository.findByBoardId(anyLong(), anyLong(), anyLong())).willReturn(boardAuthorityDto);
            given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
            given(listRepository.save(any(Lists.class))).willReturn(lists);

            // then
            assertDoesNotThrow(() -> listService.createList(userId, listCreateRequestDto));
        }
    }

    @Nested
    @DisplayName("리스트 수정 테스트 케이스")
    class UpdateListTest {
        @Test
        @DisplayName("잘못된 리스트/사용자/워크스페이스 고유번호로 인해 리스트 수정 실패")
        public void updateList_invalidId_failure() {
            // given
            long userId = 1;
            long listId = 1;

            ListUpdateRequestDto listUpdateRequestDto = mockListUpdateRequestDto();

            given(userWorkSpaceRepository.findByListId(anyLong(), anyLong(), anyLong()))
                    .willThrow(new NotFoundException(NOT_FOUND_LIST));

            // when
            Throwable t = assertThrows(NotFoundException.class,
                    () -> listService.updateList(userId, listId, listUpdateRequestDto));

            // then
            assertEquals(NOT_FOUND_LIST.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("잘못된 워크스페이스 권한으로 인해 리스트 수정 실패")
        public void updateList_forbiddenAuthority_failure() {
            // given
            long userId = 1;
            long listId = 1;

            ListUpdateRequestDto listUpdateRequestDto = mockListUpdateRequestDto();

            ListAuthorityDto listAuthorityDto = mockListAuthorityDto_READ();

            given(userWorkSpaceRepository.findByListId(anyLong(), anyLong(), anyLong())).willReturn(listAuthorityDto);

            // when
            Throwable t = assertThrows(ForbiddenException.class,
                    () -> listService.updateList(userId, listId, listUpdateRequestDto));

            // then
            assertEquals(FORBIDDEN.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("리스트 수정 성공")
        public void updateList_success() {
            // given
            long userId = 1;
            long listId = 1;

            ListUpdateRequestDto listUpdateRequestDto = mockListUpdateRequestDto();

            ListAuthorityDto listAuthorityDto = mockListAuthorityDto_EDIT();

            Lists lists = mockLists();
            String originTitle = lists.getTitle();

            given(userWorkSpaceRepository.findByListId(anyLong(), anyLong(), anyLong()))
                    .willReturn(listAuthorityDto);
            given(listRepository.findByListId(anyLong())).willReturn(lists);

            lists.updateListTitle(listUpdateRequestDto.getTitle());
            given(listRepository.save(lists)).willReturn(lists);

            ListUpdateResponseDto listUpdateResponseDto = listService.updateList(userId, listId, listUpdateRequestDto);

            // then
            assertNotEquals(originTitle, listUpdateResponseDto.getListTitle());
        }
    }

    @Nested
    @DisplayName("리스트 순서 변경 테스트 케이스")
    class UpdateListSequenceTest {
        @Test
        @DisplayName("잘못된 리스트/사용자/워크스페이스 고유번호로 인해 리스트 순서 수정 실패")
        public void updateListSequence_invalidId_failure() {
            // given
            long userId = 1;
            long listId = 1;
            int sequence = 1;

            ListUpdateRequestDto listUpdateRequestDto = mockListUpdateRequestDto();

            given(userWorkSpaceRepository.findByListId(anyLong(), anyLong(), anyLong()))
                    .willThrow(new NotFoundException(NOT_FOUND_LIST));

            // when
            Throwable t = assertThrows(NotFoundException.class,
                    () -> listService.updateListSequence(userId, listId, sequence, listUpdateRequestDto));

            // then
            assertEquals(NOT_FOUND_LIST.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("잘못된 워크스페이스 권한으로 인해 리스트 순서 수정 실패")
        public void updateListSequence_forbiddenAuthority_failure() {
            // given
            long userId = 1;
            long listId = 1;
            int sequence = 1;

            ListUpdateRequestDto listUpdateRequestDto = mockListUpdateRequestDto();

            ListAuthorityDto listAuthorityDto = mockListAuthorityDto_READ();

            given(userWorkSpaceRepository.findByListId(anyLong(), anyLong(), anyLong()))
                    .willReturn(listAuthorityDto);

            // when
            Throwable t = assertThrows(ForbiddenException.class,
                    () -> listService.updateListSequence(userId, listId, sequence, listUpdateRequestDto));

            // then
            assertEquals(FORBIDDEN.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("변경하려는 순서와 원래 순서가 같으니 종료")
        public void updateListSequence_sameSequence() {
            // given
            long userId = 1;
            long listId = 1;
            int sequence = 1;

            ListUpdateRequestDto listUpdateRequestDto = mockListUpdateRequestDto();

            ListAuthorityDto listAuthorityDto = mockListAuthorityDto_Sequence(1);

            given(userWorkSpaceRepository.findByListId(anyLong(), anyLong(), anyLong())).willReturn(listAuthorityDto);

            // when, then
            assertDoesNotThrow(() -> listService.updateListSequence(userId, listId, sequence, listUpdateRequestDto));
            assertEquals(listAuthorityDto.getSequence(), sequence);
        }

        @Test
        @DisplayName("변경하려는 순서가 리스트 개수보다 크다면 리스트 순서 변경 실패")
        public void updateListSequence_overListCount_failure() {
            // given
            long userId = 1;
            long listId = 1;
            int sequence = 100;

            ListUpdateRequestDto listUpdateRequestDto = mockListUpdateRequestDto();

            ListAuthorityDto listAuthorityDto = mockListAuthorityDto_Sequence(10);

            given(userWorkSpaceRepository.findByListId(anyLong(), anyLong(), anyLong())).willReturn(listAuthorityDto);

            // when, then
            assertThrows(InvalidParameterException.class,
                    () -> listService.updateListSequence(userId, listId, sequence, listUpdateRequestDto));
        }

        @Test
        @DisplayName("변경하려는 순서가 원래 순서보다 큼")
        public void updateListSequence_sequenceGreaterThanOrigin() {
            // given
            long userId = 1;
            long listId = 1;
            int sequence = 10;

            ListUpdateRequestDto listUpdateRequestDto = mockListUpdateRequestDto();

            ListAuthorityDto listAuthorityDto = mockListAuthorityDto_Sequence(5);

            given(userWorkSpaceRepository.findByListId(anyLong(), anyLong(), anyLong())).willReturn(listAuthorityDto);

            // when, then
            assertDoesNotThrow(() -> listService.updateListSequence(userId, listId, sequence, listUpdateRequestDto));
            verify(listRepository, times(1))
                    .updateSequenceDecrement(anyInt(), anyInt(), anyLong());
//                    .updateSequenceDecrement(sequence, listAuthorityDto.getSequence(), listAuthorityDto.getBoardId());
        }

        @Test
        @DisplayName("변경하려는 순서가 원래 순서보다 작음")
        public void updateListSequence_sequenceLessThanOrigin() {
            // given
            long userId = 1;
            long listId = 1;
            int sequence = 1;

            ListUpdateRequestDto listUpdateRequestDto = mockListUpdateRequestDto();

            ListAuthorityDto listAuthorityDto = mockListAuthorityDto_Sequence(5);

            given(userWorkSpaceRepository.findByListId(anyLong(), anyLong(), anyLong())).willReturn(listAuthorityDto);


            given(userWorkSpaceRepository.findByListId(anyLong(), anyLong(), anyLong())).willReturn(listAuthorityDto);

            // when, then
            assertDoesNotThrow(() -> listService.updateListSequence(userId, listId, sequence, listUpdateRequestDto));
            verify(listRepository, times(1)).updateSequenceIncrement(anyInt(), anyInt(), anyLong());
        }
    }

    @Nested
    @DisplayName("리스트 삭제 테스트 케이스")
    class DeleteListSequenceTest {
        @Test
        @DisplayName("잘못된 리스트/사용자/워크스페이스 고유번호로 인해 리스트 삭제 실패")
        public void deleteList_invalidId_failure() {
            // given
            long userId = 1;
            long listId = 1;

            ListDeleteRequestDto listDeleteRequestDto = mockListDeleteRequestDto();

            given(userWorkSpaceRepository.findByListId(anyLong(), anyLong(), anyLong()))
                    .willThrow(new NotFoundException(NOT_FOUND_LIST));

            // when
            Throwable t = assertThrows(NotFoundException.class,
                    () -> listService.deleteList(userId, listId, listDeleteRequestDto));

            // then
            assertEquals(NOT_FOUND_LIST.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("잘못된 워크스페이스 권한으로 인해 리스트 삭제 실패")
        public void deleteList_forbiddenAuthority_failure() {
            // given
            long userId = 1;
            long listId = 1;

            ListDeleteRequestDto listDeleteRequestDto = mockListDeleteRequestDto();

            ListAuthorityDto listAuthorityDto = mockListAuthorityDto_READ();

            given(userWorkSpaceRepository.findByListId(anyLong(), anyLong(), anyLong())).willReturn(listAuthorityDto);

            // when
            Throwable t = assertThrows(ForbiddenException.class,
                    () -> listService.deleteList(userId, listId, listDeleteRequestDto));

            // then
            assertEquals(FORBIDDEN.getMessage(), t.getMessage());
        }

        @Test
        @DisplayName("리스트 삭제 성공")
        public void deleteList_success() {
            // given
            long userId = 1;
            long listId = 1;

            ListDeleteRequestDto listDeleteRequestDto = mockListDeleteRequestDto();

            ListAuthorityDto listAuthorityDto = mockListAuthorityDto_EDIT();

            given(userWorkSpaceRepository.findByListId(anyLong(), anyLong(), anyLong())).willReturn(listAuthorityDto);

            // when, then
            assertDoesNotThrow(() -> listService.deleteList(userId, listId, listDeleteRequestDto));
        }

    }
}