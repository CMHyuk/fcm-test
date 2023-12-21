package com.example.fcm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.time.LocalDate.now;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Long saveNotification(String title, String body) {
        Notification notification = Notification.builder()
                .title(title)
                .body(body)
                .sendTime(now())
                .build();

        return notificationRepository.save(notification).getId();
    }

}
