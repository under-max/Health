package com.example.demo.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleResponse {
    private Integer id;
    private Integer memberId;
    private String memberName; //
    private Integer remainingPT; //
    private String pt;


    @Builder
    public ScheduleResponse(Integer id, Integer memberId, String memberName, Integer remainingPT, String pt) {
        this.id = id;
        this.memberId = memberId;
        this.memberName = memberName;
        this.remainingPT = remainingPT;
        this.pt = pt;
    }





    public ScheduleResponse() {

    }

}




