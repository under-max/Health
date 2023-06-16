package com.example.demo.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ChatSaveMsgRequest {
    private Integer chatId;
    private Integer memberId;
    private String memberName;
    private Integer trainerId;
    private String trainerName;
    private String message;
    private LocalDateTime timestamp;
    private Integer authority;

    @Builder
    public ChatSaveMsgRequest(Integer chatId, Integer memberId, String memberName, Integer trainerId, String trainerName, String message, LocalDateTime timestamp, Integer authority) {
        this.chatId = chatId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.trainerId = trainerId;
        this.trainerName = trainerName;
        this.message = message;
        this.timestamp = timestamp;
        this.authority = authority;
    }
}
