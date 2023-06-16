package com.example.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Chat {
    private Integer chatId;
    private Integer memberId;
    private Integer trainerId;
    private LocalDateTime timestamp;
    private String name; // memberName
    private String message;

    public Chat(Integer chatId, Integer memberId, Integer trainerId, LocalDateTime timestamp, String name, String message) {
        this.chatId = chatId;
        this.memberId = memberId;
        this.trainerId = trainerId;
        this.timestamp = timestamp;
        this.name = name;
        this.message = message;
    }
}
