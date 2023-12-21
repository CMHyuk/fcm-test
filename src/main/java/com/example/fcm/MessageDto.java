package com.example.fcm;

import lombok.Getter;

@Getter
public class MessageDto {
    private String targetToken;
    private String title;
    private String body;
}
