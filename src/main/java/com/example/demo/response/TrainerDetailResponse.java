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
    private String info;
    private Integer authority;
}
