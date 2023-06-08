package com.example.demo.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleRequest {
    private Integer memberId;
    private LocalDateTime ptTime; //


    @Builder
    public ScheduleRequest(Integer memberId, LocalDateTime ptTime) {
        this.memberId = memberId;
        this.ptTime = ptTime;
    }
}


