package com.example.demo.entity;

import lombok.Builder;
import lombok.Data;

@Data
public class CenterEntity {
    private Integer Id;
    private String name;

    private String address;

    private String info;

    private String phoneNumber;

    @Builder
    public CenterEntity(String name, String address, String info, String phoneNumber, String file){
        this.name = name;
        this.address = address;
        this.info = info;
        this.phoneNumber = phoneNumber;
    }
}
