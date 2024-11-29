package com.sparta.trelloproject.domain.workspace.controller;

import com.sparta.trelloproject.common.annotation.AuthUser;
import com.sparta.trelloproject.common.response.SuccessResponse;
import com.sparta.trelloproject.domain.workspace.dto.WorkspaceInviteRequestDto;
import com.sparta.trelloproject.domain.workspace.dto.WorkspaceRequestDto;
import com.sparta.trelloproject.domain.workspace.dto.WorkspaceResponseDto;
import com.sparta.trelloproject.domain.workspace.service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<SuccessResponse<String>> addWorkspace(
            @AuthenticationPrincipal AuthUser authUser ,
            @Valid @RequestBody WorkspaceRequestDto workSpaceRequestDto) {
        workspaceService.addWorkspace(authUser , workSpaceRequestDto);
        return ResponseEntity.ok(SuccessResponse.of(null));
    }

    @PostMapping("/invite")
    public ResponseEntity<SuccessResponse<String>> inviteMemberToWorkspace(
            @AuthenticationPrincipal AuthUser authUser ,
            @Valid @RequestBody WorkspaceInviteRequestDto workspaceInviteRequestDto) {
        workspaceService.inviteMemberToWorkspace(authUser , workspaceInviteRequestDto);
        return ResponseEntity.ok(SuccessResponse.of(null));
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<WorkspaceResponseDto>>> getWorkspaces(
            @AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(SuccessResponse.of(workspaceService.getWorkspaces(authUser)));
    }

    @PatchMapping("/{workspaceId}")
    public ResponseEntity<SuccessResponse<String>> editWorkspace(
            @AuthenticationPrincipal AuthUser authUser ,
            @PathVariable Long workspaceId ,
            @Valid @RequestBody WorkspaceRequestDto workSpaceRequestDto) {
        workspaceService.editWorkspace(authUser , workspaceId , workSpaceRequestDto);
        return ResponseEntity.ok(SuccessResponse.of(null));
    }

    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<SuccessResponse<String>> removeWorkspace(
            @AuthenticationPrincipal AuthUser authUser ,
            @PathVariable Long workspaceId) {
        workspaceService.removeWorkspace(authUser , workspaceId);
        return ResponseEntity.ok(SuccessResponse.of(null));
    }
}
