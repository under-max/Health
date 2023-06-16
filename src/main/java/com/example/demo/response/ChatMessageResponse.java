package com.example.demo.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ChatMessageResponse {
    private Integer chatId;
    private Integer messageFrom;
    private Integer messageTo;
    private String timestamp;
    private String memberName;
    private String message;
    private String trainerName;
    private Integer authority;

    @Builder
    public ChatMessageResponse(Integer chatId, Integer messageFrom, Integer messageTo, String timestamp, String memberName, String message, String trainerName, Integer authority) {
        this.chatId = chatId;
        this.messageFrom = messageFrom;
        this.messageTo = messageTo;
        this.timestamp = timestamp;
        this.memberName = memberName;
        this.message = message;
        this.trainerName = trainerName;
        this.authority = authority;
    }
}
