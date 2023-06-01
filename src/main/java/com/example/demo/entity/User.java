package com.example.demo.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    private Long id;
    private String name;
    private String address;
    private String email;
    private String password;
    private Date birthDate;
    private Long centerId;
    private Long trainerId;
    private boolean isInCenter;
    private String nickName;
    private LocalDateTime dateTime;
    private Integer authority;

    @Builder
    public User(Long id, String name, String address, String email, String password, Date birthDate, Long centerId, Long trainerId, boolean isInCenter, String nickName, LocalDateTime dateTime, Integer authority) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.centerId = centerId;
        this.trainerId = trainerId;
        this.isInCenter = isInCenter;
        this.nickName = nickName;
        this.dateTime = dateTime;
        this.authority = authority;
    }
}
