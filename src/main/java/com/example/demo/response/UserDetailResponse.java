package com.example.demo.response;

import com.example.demo.entity.Membership;
import lombok.Data;

import java.sql.Date;

@Data
public class UserDetailResponse {

    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private Integer gender;
    private Integer age;
    private Integer isInCenter;
    private String trainerID;
    private String trainerName;
    private String centerName;
    private Date startDate;
    private Date endDate;
    private Integer remainingPT;


    //private Membership membership;
}
