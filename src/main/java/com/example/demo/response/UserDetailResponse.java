package com.example.demo.response;

import com.example.demo.entity.Membership;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

@Data
public class UserDetailResponse {

    private String id;
    private String name;
    private String email;
    private Integer isInCenter;
    private String trainerID;
    private String trainerName;
    private String centerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer remainingPT;

    //private Membership membership;
}
