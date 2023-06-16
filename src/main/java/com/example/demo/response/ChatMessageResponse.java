package com.example.demo.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ChatMessageResponse {
    private Integer chatId;
    private Integer memberId;
    private LocalDateTime timestamp;
    private String memberName;
    private String message;
    private String trainerName;
    private Integer authority;

    @Builder
    public ChatMessageResponse(Integer chatId, Integer memberId, LocalDateTime timestamp, String memberName, String message, String trainerName, Integer authority) {
        this.chatId = chatId;
        this.memberId = memberId;
        this.timestamp = timestamp;
        this.memberName = memberName;
        this.message = message;
        this.trainerName = trainerName;
        this.authority = authority;
    }
}
