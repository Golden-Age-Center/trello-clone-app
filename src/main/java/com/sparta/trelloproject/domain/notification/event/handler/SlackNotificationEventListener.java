package com.sparta.trelloproject.domain.notification.event.handler;


import com.sparta.trelloproject.domain.notification.enums.NotificationMessage;
import com.sparta.trelloproject.domain.notification.event.InvitedWorkspaceEvent;
import com.sparta.trelloproject.domain.notification.event.SavedCommentEvent;
import com.sparta.trelloproject.domain.notification.event.UpdatedCardEvent;
import com.sparta.trelloproject.domain.notification.service.SlackBotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SlackNotificationEventListener {

    private final SlackBotService slackBotService;
    private final String channelId="C07S2HXJ5U2";


    @EventListener
    public void handleSlackSavedCommentEvent(SavedCommentEvent event){
        //슬랙 봇에 메시지 전송
        slackBotService.sendSlackMessage(channelId, NotificationMessage.ADDED_COMMENT.getMessage());
    }
    @EventListener
    public void handleSlackInvitedWorkspaceEvent(InvitedWorkspaceEvent event){
        //슬랙 봇에 메시지 전송
        slackBotService.sendSlackMessage(channelId, NotificationMessage.INVITE_WORKSPACE.getMessage());
    }
    @EventListener
    public void handleSlackUpdateCardEvent(UpdatedCardEvent event){
        //슬랙 봇에 메시지 전송
        slackBotService.sendSlackMessage(channelId, NotificationMessage.UPDATE_CARD.getMessage());
    }
}
