package com.sparta.trelloproject.domain.list.dto;

import com.sparta.trelloproject.domain.workspace.enums.WorkSpaceUserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListAuthorityDto {
    private long listId;
    private WorkSpaceUserRole workspaceAuthority;
    private long boardId;
    private int sequence;
    private String title;
    private long listCount;
}
