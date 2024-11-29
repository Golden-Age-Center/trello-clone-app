package com.sparta.trelloproject.domain.notification.event;

public class InvitedWorkspaceEvent {
    private final Long userId;

    public InvitedWorkspaceEvent(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
