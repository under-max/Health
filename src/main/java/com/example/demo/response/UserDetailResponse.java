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

    private Integer id;
    private String name;
    private Integer isInCenter;
    private Integer trainerId;
    private String trainerName;
    private Integer centerId;
    private String centerName;
    private String centerAddress;
    private String startDate;
    private String endDate;
    private Integer remainingPT;
    private Integer authority;

    public UserDetailResponse(Integer id, String name, Integer isInCenter, Integer trainerId, String trainerName, Integer centerId, String centerName, String centerAddress, String startDate, String endDate, Integer remainingPT, Integer authority) {
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
        this.authority = authority;
    }
}
