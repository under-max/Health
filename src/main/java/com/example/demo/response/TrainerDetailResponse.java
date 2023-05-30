package com.example.demo.response;

import lombok.Data;

@Data
public class TrainerDetailResponse {

    private Integer id; //
    private String name;
    private String address; //
    private String email;
    private String phoneNumber;
    private Integer centerId;

    private String info;
    private Integer isInCenter;
}
