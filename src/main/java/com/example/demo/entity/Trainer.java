package com.example.demo.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trainer {

    private Integer id;
    private Integer memberId;
    private String trainerName;
    private String name;
    private Integer centerId;
    private String info;

}
