package com.example.demo.response.membership;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
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
