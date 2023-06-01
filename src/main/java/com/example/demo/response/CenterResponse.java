package com.example.demo.response;

import com.example.demo.entity.Center;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class CenterResponse {
    private Integer id;
    private String name;

    private String address;

    private String info;

    private String phoneNumber;

    private List<String> fileName;

    public CenterResponse(Center center) {
        this.id = center.getId();
        this.name = center.getName();
        this.address = center.getAddress();
        this.info = center.getInfo();
        this.phoneNumber = center.getPhoneNumber();
        this.fileName = center.getFileName();
    }


    public CenterResponse(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    @Builder
    public CenterResponse(Integer id, String name, String address, String info, String phoneNumber, List<String> fileName){
        this.id = id;
        this.name = name;
        this.address = address;
        this.info = info;
        this.phoneNumber = phoneNumber;
        this.fileName = fileName;
    }
}
