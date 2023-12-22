package com.example.fcm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@RequiredArgsConstructor
public class FirebaseCloudMessageService {

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/fcmtest-2ba1b/messages:send";
    private final ObjectMapper objectMapper;

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/firebase_service_key.json";
        GoogleCredentials googleCredentials = GoogleCredentials.
                fromStream(new ClassPathResource(firebaseConfigPath).getInputStream()).
                createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();

        String tokenValue = googleCredentials.
                getAccessToken().
                getTokenValue();

        System.out.println("tokenValue = " + tokenValue);

        return tokenValue;
    }

    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = makeMessage(targetToken, title, body);

        //.retrieve(): HTTP 요청을 보냅니다. 그리고 나서, 응답을 받기 위한 준비를 합니다.
        //.bodyToMono(String.class): 응답 본문을 Mono<String> 형태로 변환합니다.
        // - >이는 비동기적으로 받은 응답을 처리할 수 있도록 합니다. 여기서 Mono는 Reactor 라이브러리의 데이터 타입으로, 비동기적인 데이터 스트림을 나타냅니다.
        //.doOnSuccess(response -> System.out.println("FCM Response: " + response))
        // -> 응답을 받았을 때 수행할 동작을 정의합니다. 여기서는 응답을 콘솔에 출력하는 동작을 정의하고 있습니다.
        //.subscribe(): 비동기적으로 요청을 보내고 응답을 받기 위해 구독(subscribe)합니다. 이는 비동기 코드를 실행시키는 부분입니다.

        // FCM 메시지 전송
        WebClient.create()
                .post()
                .uri(API_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .contentType(APPLICATION_JSON)
                .body(BodyInserters.fromValue(message))
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> System.out.println("FCM Response: " + response))
                .subscribe();
    }

    private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build())
                        .build())
                .validate_only(false)
                .build();
        return objectMapper.writeValueAsString(fcmMessage);
    }

}
