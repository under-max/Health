package com.example.demo.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ChatSaveMsgRequest {
    private Integer from;
    private Integer to;
    private String message;




}
