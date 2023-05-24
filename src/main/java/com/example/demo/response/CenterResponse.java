package com.example.demo.response;

import com.example.demo.entity.CenterEntity;
import lombok.Builder;
import lombok.Data;

@Data
public class CenterResponse {
    private Integer id;
    private String name;

    private String address;

    private String info;

    private String phoneNumber;

    private String file;

    public CenterResponse(CenterEntity centerEntity) {
        this.id = centerEntity.getId();
        this.name = centerEntity.getName();
        this.address = centerEntity.getAddress();
        this.info = centerEntity.getInfo();
        this.phoneNumber = centerEntity.getPhoneNumber();
    }

    @Builder
    public CenterResponse(Integer id, String name, String address, String info, String phoneNumber, String file){
        this.id = id;
        this.name = name;
        this.address = address;
        this.info = info;
        this.phoneNumber = phoneNumber;
        this.file = file;
    }
}
