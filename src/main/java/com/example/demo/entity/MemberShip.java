package com.example.demo.entity;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
public class MemberShip {

    private Integer membershipId;
    private Integer memberId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer remainingPT;

}
