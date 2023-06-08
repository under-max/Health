package com.example.demo.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleResponse {
    private String memberName; //
    private LocalDateTime ptTime; //
    private Integer remainingPT; //


    @Builder
    public ScheduleResponse(String memberName, LocalDateTime ptTime, Integer remainingPT) {
        this.memberName = memberName;
        this.ptTime = ptTime;
        this.remainingPT = remainingPT;
    }


    public ScheduleResponse() {

    }
}




