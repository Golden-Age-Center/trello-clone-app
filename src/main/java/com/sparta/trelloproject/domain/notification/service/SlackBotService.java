package com.sparta.trelloproject.domain.notification.service;


import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
@RequiredArgsConstructor
public class SlackBotService {

    @Value("${SLACKBOT_TOKEN}")
    private String SLACKBOT_TOKEN;

    private static final String SLACK_WEBHOOK_URL = "https://slack.com/api/chat.postMessage";

    // 슬랙 채널에 메시지를 보내는 메서드
    public void sendSlackMessage(String channelId,String message){
        RestTemplate restTemplate=new RestTemplate();

        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(SLACKBOT_TOKEN);

        //슬랙 메시지 전송을 위한 JSON
        JSONObject json=new JSONObject();
        json.put("channel",channelId); // 메시지를 보낼 ID
        json.put("text",message); //전송할 메시지

        HttpEntity<String> entity=new HttpEntity<>(json.toString(),headers);

        //슬랙 API 호출
        ResponseEntity<String> response=restTemplate.exchange(
                SLACK_WEBHOOK_URL,
                HttpMethod.POST,
                entity,
                String.class
        );

        if(response.getStatusCode().is2xxSuccessful()){
            log.info("슬랙 메시지 전송 성공={}",response.getBody());
        }else{
            log.info("슬랙 메시지 전송 실패={}",response.getBody());
        }
    }
}
