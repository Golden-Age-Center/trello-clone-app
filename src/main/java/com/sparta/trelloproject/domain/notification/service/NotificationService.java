package com.sparta.trelloproject.domain.notification.service;


import com.sparta.trelloproject.common.exception.ApiException;
import com.sparta.trelloproject.common.exception.NotFoundException;
import com.sparta.trelloproject.common.exception.ResponseCode;
import com.sparta.trelloproject.domain.notification.entity.Notification;
import com.sparta.trelloproject.domain.notification.enums.NotificationMessage;
import com.sparta.trelloproject.domain.notification.enums.NotificationType;
import com.sparta.trelloproject.domain.notification.event.SavedCommentEvent;
import com.sparta.trelloproject.domain.notification.repository.EmitterRepository;
import com.sparta.trelloproject.domain.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    //SSE 이벤트 타임아웃 시간
    private static final Long DEFAULT_TIMEOUT=60L*1000*60;
    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;
    private final SlackBotService slackBotService;

    private final String channelId="C07S2HXJ5U2";
    /**
     * 클라이언트가 구독을 위해 호출하는 메서드
     * 사용자 아이디를 기반으로 이벤트 Emitter를 생성
     * @param targetId - 구독하는 클라이언트의 사용자 아이디
     * @return SseEmitter -서버에서 보낸 이벤트
     */
    public SseEmitter subscribe(Long targetId) {
        SseEmitter sseEmitter=emitterRepository.save(targetId,new SseEmitter(DEFAULT_TIMEOUT)); //사용자 id와 emitter를 매핑

        // 사용자에게 모든 데이터가 전송되었다면 emitter 삭제
        sseEmitter.onCompletion(()-> emitterRepository.deleteById(targetId));
        // Emitter의 유효 시간이 만료되면 emitter 삭제
        sseEmitter.onTimeout(()-> emitterRepository.deleteById(targetId));

        //첫 구독 시에 이벤트를 발생시킨다
        //SEE 연결이 이루어진 후, 하나의 데이터로 전송되지 않는다면 SSE 유효시간이 만료되고 504에러 발생
        sendToClient(targetId, NotificationMessage.SUCCESS_SUBSCRIBE.getMessage(), NotificationType.SUBSCRIBE);

        return sseEmitter;
    }

    /**
     * 이벤트가 구독되어 있는 클라이언트에게 데이터를 전송
     */
    public void sendNotification(Long targetId, String content, NotificationType notificationType){
        sendToClient(targetId,content,notificationType);
    }

    /**
     * 서버의 이벤트를 클라이언트에게 보내는 메서드
     * 다른 서비스 로직에서 이 메서드를 사용해 데이터를 Object event에 넣고 전송하면 된다
     */
    public void sendToClient(Long targetId, String content,NotificationType notificationType) {

        Notification notification=new Notification(targetId,content,notificationType);
        notificationRepository.save(notification);

        SseEmitter sseEmitter= emitterRepository.findById(targetId);
        log.info("sseEmitter: "+sseEmitter);

        if(sseEmitter!=null){
            try{
                sseEmitter.send(
                        SseEmitter.event()
                                .id(targetId.toString())
                                .name("notification")
                                .data(notification)
                );
                log.info("[알림 전송 완료 : userId={}]",targetId);
                //슬랙 봇에 메시지 전송
                // slackBotService.sendSlackMessage(channelId,content);

            }catch(IOException ex){
                log.info("[알림 전송 실패 : userId={}]",targetId);
                emitterRepository.deleteById(targetId); //전송 실패 시 구독 취소
                throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,ResponseCode.CONNECTION_ERROR.getMessage());
            }
        }
//        else{
//            throw new NotFoundException(ResponseCode.NOT_FOUND_EMITTER);
//        }

    }
}
