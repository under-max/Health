package com.example.demo.request.schedule;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DateRequest {
    private Integer year;
    private Integer month;


    @Builder
    public DateRequest(Integer year, Integer month) {
        this.year = year;
        this.month = month;
    }
}
