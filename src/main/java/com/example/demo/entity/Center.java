package com.example.demo.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Center {
    private Integer id;

    private String name;

    private String address;

    private String info;

    private String phoneNumber;

    private Double star;

    private List<Long> trainerId;

    private List<String> fileName;

    private List<String> trainerName;
    @Builder
    public Center(Integer id, String name, String address, String info, String phoneNumber, String file, Double star, List<Long> trainerId, List<String> fileName, List<String> trainerName){
        this.id = id;
        this.name = name;
        this.address = address;
        this.info = info;
        this.phoneNumber = phoneNumber;
        this.fileName = fileName;
        this.trainerName = trainerName;
        this.trainerId = trainerId;
        this.star = star;
    }
}
