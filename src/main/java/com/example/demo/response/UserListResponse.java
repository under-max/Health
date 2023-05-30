package com.example.demo.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserListResponse {

    private Integer id;
    private String name;
    private LocalDate birthDate;
    private String trainerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer remainingPT;


}
