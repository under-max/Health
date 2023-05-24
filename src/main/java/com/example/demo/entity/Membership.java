package com.example.demo.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class Membership {

    private Integer membershipId;
    private Integer memberId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer remainingPT;

    public Membership() {
    }

    @Builder
    public Membership(Integer memberId, LocalDate startDate, LocalDate endDate, Integer remainingPT) {
        this.memberId = memberId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.remainingPT = remainingPT;
    }
}
