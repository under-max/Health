package com.example.demo.entity;

import lombok.Data;

@Data
public class Trainer {
    private Long id;
    private String name;
    private String address;
    private String email;
    private String password;
    private String phoneNumber;
    private Long centerId;
    private String info;
    private String isInCenter;
}
