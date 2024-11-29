package com.sparta.trelloproject.domain.card.dto;

import com.sparta.trelloproject.domain.workspace.enums.WorkSpaceUserRole;
import lombok.Getter;

@Getter
public class CardAuthorityDto {

    private Long cardId;
    private WorkSpaceUserRole workspaceAuthority;
    private long listId;
    private String title;
    private String card_Status;
    private String contents;
}
