package com.example.demo.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ChatSaveMsgRequest {
    private Integer messageFrom;
    private Integer messageTo;
    private String message;

    @Builder
    public ChatSaveMsgRequest(Integer messageFrom, Integer messageTo, String message) {
        this.messageFrom = messageFrom;
        this.messageTo = messageTo;
        this.message = message;
    }
}
