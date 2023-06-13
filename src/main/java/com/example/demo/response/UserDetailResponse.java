package com.example.demo.response;

import com.example.demo.entity.Membership;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserDetailResponse {

    private String id;
    private String name;
    private Integer isInCenter;
    private String trainerId;
    private String trainerName;
    private String centerId;
    private String centerName;
    private String centerAddress;
    private String startDate;
    private String endDate;
    private Integer remainingPT;

    @Builder
    public UserDetailResponse(String id, String name, Integer isInCenter, String trainerId, String trainerName, String centerId, String centerName, String centerAddress, String startDate, String endDate, Integer remainingPT) {
        this.id = id;
        this.name = name;
        this.isInCenter = isInCenter;
        this.trainerId = trainerId;
        this.trainerName = trainerName;
        this.centerId = centerId;
        this.centerName = centerName;
        this.centerAddress = centerAddress;
        this.startDate = startDate;
        this.endDate = endDate;
        this.remainingPT = remainingPT;
    }
}
