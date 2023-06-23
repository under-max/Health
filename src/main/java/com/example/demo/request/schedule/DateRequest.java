package com.example.demo.request.schedule;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DateRequest {
    private Integer memberId;
    private Integer year;
    private Integer month;


    public DateRequest(Integer memberId, Integer year, Integer month) {
        this.memberId = memberId;
        this.year = year;
        this.month = month;
    }
}
