package com.example.fcm;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    @Id @GeneratedValue
    @Column(name = "notification_id")
    private Long id;

    private String title;
    private String body;
    private LocalDate sendTime;

    @Builder
    public Notification(String title, String body, LocalDate sendTime) {
        this.title = title;
        this.body = body;
        this.sendTime = sendTime;
    }

}
