package com.example.demo.response.membership;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MembershipResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer remainingPT;

    @Builder
    public MembershipResponse(LocalDate startDate, LocalDate endDate, Integer remainingPT) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.remainingPT = remainingPT;
    }
}
