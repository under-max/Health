package com.example.demo.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ChatSaveMsgRequest {
    private Integer memberId;
    private Integer trainerId;
    private String message;

    @Builder
    public ChatSaveMsgRequest(Integer memberId, Integer trainerId, String message) {
        this.memberId = memberId;
        this.trainerId = trainerId;
        this.message = message;
    }
}
