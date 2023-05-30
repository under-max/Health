package com.example.demo.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Center {
    private Integer Id;
    private String name;

    private String address;

    private String info;

    private String phoneNumber;

    private List<String> fileName;

    @Builder
    public Center(String name, String address, String info, String phoneNumber, String file, List<String> fileName){
        this.name = name;
        this.address = address;
        this.info = info;
        this.phoneNumber = phoneNumber;
        this.fileName = fileName;
    }
}
