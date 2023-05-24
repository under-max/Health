package com.example.demo.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    private Long id;
    private String name;
    private String address;
    private String email;
    private String password;
    private String phoneNumber;
    private String gender;
    private Long age;
    private Long centerId;
    private Long trainerId;
    private boolean isInCenter;
    private LocalDateTime dateTime;
    @Builder

    public User(Long id, String name, String address, String email, String password, String phoneNumber, String gender, Long age, Long centerId, Long trainerId, boolean isInCenter, LocalDateTime dateTime) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.age = age;
        this.centerId = centerId;
        this.trainerId = trainerId;
        this.isInCenter = isInCenter;
        this.dateTime = dateTime;
    }
}
