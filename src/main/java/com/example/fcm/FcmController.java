package com.example.fcm;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FcmController {

    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final NotificationService notificationService;

    @PostMapping("/send-message")
    public Long sendMessage(@RequestBody MessageDto messageDto) throws IOException {
        firebaseCloudMessageService.sendMessageTo(messageDto.getTargetToken(), messageDto.getTitle(), messageDto.getBody());
        return notificationService.saveNotification(messageDto.getTitle(), messageDto.getBody());
    }

}
