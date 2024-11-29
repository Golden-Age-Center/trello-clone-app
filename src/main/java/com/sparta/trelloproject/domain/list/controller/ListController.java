package com.sparta.trelloproject.domain.list.controller;

import com.sparta.trelloproject.common.annotation.AuthUser;
import com.sparta.trelloproject.common.response.SuccessResponse;
import com.sparta.trelloproject.domain.list.dto.request.ListCreateRequestDto;
import com.sparta.trelloproject.domain.list.dto.request.ListDeleteRequestDto;
import com.sparta.trelloproject.domain.list.dto.request.ListUpdateRequestDto;
import com.sparta.trelloproject.domain.list.dto.response.ListCreateResponseDto;
import com.sparta.trelloproject.domain.list.dto.response.ListUpdateResponseDto;
import com.sparta.trelloproject.domain.list.service.ListService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ListController {
    private final ListService listService;

    @PostMapping("/v1/lists")
    public ResponseEntity<SuccessResponse<ListCreateResponseDto>> createList(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody ListCreateRequestDto listCreateRequestDto
    ) {
        ListCreateResponseDto listCreateResponseDto =
                listService.createList(authUser.getUserId(), listCreateRequestDto);
        return ResponseEntity.ok(SuccessResponse.of(listCreateResponseDto));
    }

    @PatchMapping("/v1/lists/{listId}")
    public ResponseEntity<SuccessResponse<ListUpdateResponseDto>> updateList(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable long listId,
            @Valid @RequestBody ListUpdateRequestDto listUpdateRequestDto
    ) {
        ListUpdateResponseDto listUpdateResponseDto =
                listService.updateList(authUser.getUserId(), listId, listUpdateRequestDto);
        return ResponseEntity.ok(SuccessResponse.of(listUpdateResponseDto));
    }

    @PatchMapping("/v1/lists/{listId}/sequence/{sequence}")
    public ResponseEntity<SuccessResponse<Void>> updateListSequence(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable long listId,
            @Min(value = 1, message = "순서는 1 이상이어야 합니다.") @PathVariable int sequence,
            @Valid @RequestBody ListUpdateRequestDto listUpdateRequestDto
    ) {
        listService.updateListSequence(authUser.getUserId(), listId, sequence, listUpdateRequestDto);
        return ResponseEntity.ok(SuccessResponse.of(null));
    }

    @DeleteMapping("/v1/lists/{listId}")
    public ResponseEntity<SuccessResponse<Void>> deleteList(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable long listId,
            @Valid @RequestBody ListDeleteRequestDto listDeleteRequestDto
    ) {
        listService.deleteList(authUser.getUserId(), listId, listDeleteRequestDto);
        return ResponseEntity.ok(SuccessResponse.of(null));
    }

}
