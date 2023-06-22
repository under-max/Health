package com.example.demo.request.schedule;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleRequest {
    private Integer Id;
    private Integer memberId;
    private LocalDateTime ptTime; //
    private Integer remainingPT;


    public ScheduleRequest() {

    }

    @Builder

    public ScheduleRequest(Integer id, Integer memberId, LocalDateTime ptTime, Integer remainingPT) {
        Id = id;
        this.memberId = memberId;
        this.ptTime = ptTime;
        this.remainingPT = remainingPT;
    }
}


