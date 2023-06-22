package com.example.demo.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChatSaveMsgDTO {
    private Integer messageFrom;
    private Integer messageTo;
    private String message;

    public ChatSaveMsgDTO(Integer messageFrom, Integer messageTo, String message) {
        this.messageFrom = messageFrom;
        this.messageTo = messageTo;
        this.message = message;
    }
}
