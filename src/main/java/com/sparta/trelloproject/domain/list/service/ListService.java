package com.sparta.trelloproject.domain.list.service;

import com.sparta.trelloproject.common.exception.ForbiddenException;
import com.sparta.trelloproject.common.exception.InvalidParameterException;
import com.sparta.trelloproject.domain.board.dto.BoardAuthorityDto;
import com.sparta.trelloproject.domain.board.entity.Board;
import com.sparta.trelloproject.domain.board.repository.BoardRepository;
import com.sparta.trelloproject.domain.list.dto.ListAuthorityDto;
import com.sparta.trelloproject.domain.list.dto.request.ListCreateRequestDto;
import com.sparta.trelloproject.domain.list.dto.request.ListDeleteRequestDto;
import com.sparta.trelloproject.domain.list.dto.request.ListUpdateRequestDto;
import com.sparta.trelloproject.domain.list.dto.response.ListCreateResponseDto;
import com.sparta.trelloproject.domain.list.dto.response.ListUpdateResponseDto;
import com.sparta.trelloproject.domain.list.entity.Lists;
import com.sparta.trelloproject.domain.list.repository.ListRepository;
import com.sparta.trelloproject.domain.workspace.enums.WorkSpaceUserRole;
import com.sparta.trelloproject.domain.workspace.repository.UserWorkSpaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.trelloproject.common.exception.ResponseCode.FORBIDDEN;
import static com.sparta.trelloproject.common.exception.ResponseCode.INVALID_LIST_SEQUENCE;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ListService {
    private final UserWorkSpaceRepository userWorkSpaceRepository;
    private final BoardRepository boardRepository;
    private final ListRepository listRepository;

    public ListCreateResponseDto createList(long userId, ListCreateRequestDto listCreateRequestDto) {
        BoardAuthorityDto boardAuthorityDto =
                userWorkSpaceRepository.findByBoardId(listCreateRequestDto.getWorkspaceId(), userId, listCreateRequestDto.getBoardId());

        if (boardAuthorityDto.getWorkspaceAuthority().getSeq() > WorkSpaceUserRole.ROLE_EDIT_USER.getSeq()) {
            throw new ForbiddenException(FORBIDDEN);
        }

        Board board = boardRepository.findById(boardAuthorityDto.getBoardId()).get();

        int sequence = boardAuthorityDto.getListCount() + 1;
        Lists list = Lists.of(listCreateRequestDto.getTitle(), sequence, board);

        return ListCreateResponseDto.from(listRepository.save(list));
    }

    public ListUpdateResponseDto updateList(long userId, long listId, ListUpdateRequestDto listUpdateRequestDto) {
        validateUserAuthority(listUpdateRequestDto.getWorkspaceId(), userId, listId);

        Lists lists = listRepository.findByListId(listId);

        if (Strings.isNotBlank(listUpdateRequestDto.getTitle())) {
            lists.updateListTitle(listUpdateRequestDto.getTitle());
        }

        return ListUpdateResponseDto.from(listRepository.save(lists));
    }

    public void updateListSequence(long userId, long listId, int sequence, ListUpdateRequestDto listUpdateRequestDto) {
        ListAuthorityDto listAuthorityDto = validateUserAuthority(listUpdateRequestDto.getWorkspaceId(), userId, listId);

        if (listAuthorityDto.getSequence() == sequence)
            return;

        if (listAuthorityDto.getListCount() < sequence) {
            throw new InvalidParameterException(INVALID_LIST_SEQUENCE);
        }

        if (sequence < listAuthorityDto.getSequence()) {
            listRepository.updateSequenceIncrement(sequence, listAuthorityDto.getSequence(), listAuthorityDto.getBoardId());
        } else {
            listRepository.updateSequenceDecrement(sequence, listAuthorityDto.getSequence(), listAuthorityDto.getBoardId());
        }
        listRepository.updateSequence(listId, sequence);
    }

    public void deleteList(long userId, long listId, ListDeleteRequestDto listDeleteRequestDto) {
        ListAuthorityDto listAuthorityDto = validateUserAuthority(listDeleteRequestDto.getWorkspaceId(), userId, listId);

        listRepository.deleteById(listId);
        listRepository.updateSequenceDecrement(null, listAuthorityDto.getSequence(), listAuthorityDto.getBoardId());
    }

    private ListAuthorityDto validateUserAuthority(long workspaceId, long userId, long listId) {
        ListAuthorityDto listAuthorityDto = userWorkSpaceRepository.findByListId(workspaceId, userId, listId);

        if (listAuthorityDto.getWorkspaceAuthority().getSeq() > WorkSpaceUserRole.ROLE_EDIT_USER.getSeq()) {
            throw new ForbiddenException(FORBIDDEN);
        }

        return listAuthorityDto;
    }
}
