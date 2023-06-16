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


    private Double star;

    private List<Long> trainerId;

    private List<String> fileName;

    private List<String> trainerName;

    public CenterResponse(Center center) {
        this.id = center.getId();
        this.name = center.getName();
        this.address = center.getAddress();
        this.info = center.getInfo();
        this.phoneNumber = center.getPhoneNumber();
        this.fileName = center.getFileName();
        this.trainerName = center.getTrainerName();
        this.trainerId = center.getTrainerId();
        this.star = center.getStar();
    }


    public CenterResponse(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    @Builder
    public CenterResponse(Integer id, String name, String address, String info, String phoneNumber,List<Long> trainerId, List<String> fileName, List<String> trainerName){
        this.id = id;
        this.name = name;
        this.address = address;
        this.info = info;
        this.phoneNumber = phoneNumber;
        this.fileName = fileName;
        this.trainerName = trainerName;
        this.trainerId = trainerId;
    }
}
