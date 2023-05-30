package com.example.demo.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trainer {

    private Integer id;
    private String name;
    private String address;
    private String email;
    private String password;
    private String phoneNumber;
    private Integer centerId;
    private String info;
    private Integer isInCenter;


}
