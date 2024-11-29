package com.sparta.trelloproject.domain.notification.event.handler;

import com.sparta.trelloproject.domain.card.entity.Manager;
import com.sparta.trelloproject.domain.card.repository.ManagerRepository;
import com.sparta.trelloproject.domain.notification.enums.NotificationMessage;
import com.sparta.trelloproject.domain.notification.enums.NotificationType;
import com.sparta.trelloproject.domain.notification.event.InvitedWorkspaceEvent;
import com.sparta.trelloproject.domain.notification.event.SavedCommentEvent;
import com.sparta.trelloproject.domain.notification.event.UpdatedCardEvent;
import com.sparta.trelloproject.domain.notification.service.NotificationService;
import com.sparta.trelloproject.domain.notification.service.SlackBotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

    private final NotificationService notificationService;
    private final ManagerRepository managerRepository;


    /**
     * 댓글 저장 알림 전송 이벤트
     * @param event
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSavedCommentEvent(SavedCommentEvent event){
        //알림 서비스 로직 호출
        //카드의 매니저들 조회
        List<Manager> cardManagers=managerRepository.findManagersByCard_Id(event.getCardId());
        for (Manager cardManager : cardManagers) {
            if(!cardManager.getId().equals(event.getUserId())){
                notificationService.sendNotification(cardManager.getId(), NotificationMessage.ADDED_COMMENT.getMessage(), NotificationType.COMMENT);
            }
        }

    }

    /**
     * 워크스페이스 초대 알림 전송 이벤트
     * @param event
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleInvitedWorkspaceEvent(InvitedWorkspaceEvent event){
        notificationService.sendNotification(event.getUserId(), NotificationMessage.INVITE_WORKSPACE.getMessage(), NotificationType.WORKSPACE);
    }


    /**
     * 카드 수정 알림 전송 이벤트
     * @param event
     */

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUpdateCardEvent(UpdatedCardEvent event){
        List<Manager> cardManagers=managerRepository.findManagersByCard_Id(event.getCardId());
        for (Manager cardManager : cardManagers) {
            if(!cardManager.getId().equals(event.getUserId())){
                notificationService.sendNotification(cardManager.getId(), NotificationMessage.UPDATE_CARD.getMessage(), NotificationType.CARD);
            }
        }
    }

}
