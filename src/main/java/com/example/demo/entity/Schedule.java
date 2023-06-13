package com.example.demo.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Schedule {
    private Integer id;
    private Integer memberId;
    private String memberName; //
    private LocalDateTime ptTime; //
    private Integer remainingPT; //


    @Builder
    public Schedule(Integer id, Integer memberId, String memberName, LocalDateTime ptTime, Integer remainingPT) {
        this.id = id;
        this.memberId = memberId;
        this.memberName = memberName;
        this.ptTime = ptTime;
        this.remainingPT = remainingPT;
    }





    public Schedule() {

    }


}




