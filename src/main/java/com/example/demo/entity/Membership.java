package com.example.demo.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Membership {

    private Integer membershipId;
    private Integer memberId;
    private String startDate;
    private String endDate;
    private Integer remainingPT;

    public Membership() {
    }

    @Builder
    public Membership(Integer memberId, String startDate, String endDate, Integer remainingPT) {
        this.memberId = memberId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.remainingPT = remainingPT;
    }
}
