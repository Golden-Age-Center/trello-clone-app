package com.sparta.trelloproject.domain.notification.controller;

import com.sparta.trelloproject.common.annotation.AuthUser;
import com.sparta.trelloproject.common.response.SuccessResponse;
import com.sparta.trelloproject.domain.notification.enums.NotificationType;
import com.sparta.trelloproject.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    /**
     * 클라이언트의 이벤트 구독을 수락한다
     * @param authUser
     * @return
     */
    //SSE 연결 설정
    @GetMapping(value="/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE) //서버가 클라이언트에게 이벤트 스트림 전송
    public ResponseEntity<SseEmitter> subscribe(@AuthenticationPrincipal AuthUser authUser){
        if (authUser == null) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated.");
        }
        SseEmitter sseEmitter=notificationService.subscribe(authUser.getUserId());
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(sseEmitter);
    }


    /**
     * 이벤트를 구독 중인 클라이언트들에게 데이터를 전송한다
     */
    @PostMapping("/sendData")
    public void sendData(@AuthenticationPrincipal AuthUser authUser, String content, NotificationType notificationType){
        notificationService.sendNotification(authUser.getUserId(),content,notificationType);
    }
}
