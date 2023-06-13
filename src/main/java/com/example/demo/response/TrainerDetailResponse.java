package com.example.demo.response;

import lombok.Data;

@Data
public class TrainerDetailResponse {

    private Integer id;
    private Integer memberId;
    private String name;
    private Integer centerId;
    private String centerName;
    private String centerAddress;
    private String centerInfo;
    private String trainerInfo;
    private String infoDetail;
    private Integer authority;
}
