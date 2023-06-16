package com.example.demo.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ScheduleUpdateRequest {
    private Integer id;
    private Integer memberId;
    private LocalDateTime ptHour;

    @Builder
    public ScheduleUpdateRequest(Integer id, Integer memberId, LocalDateTime ptHour) {
        this.id = id;
        this.memberId = memberId;
        this.ptHour = ptHour;
    }
}
