package com.example.demo.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserListResponse {

    private Integer id;
    private String name;
    private LocalDate birthDate;
    private Integer trainerId;
    private String trainerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer remainingPT;
    private Integer isCenter;
    private Integer authority;

    public UserListResponse() {
    }

    @Builder
    public UserListResponse(Integer id, String name, LocalDate birthDate, String trainerName, LocalDate startDate, LocalDate endDate, Integer remainingPT, Integer isCenter, Integer authority) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.trainerName = trainerName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.remainingPT = remainingPT;
        this.isCenter = isCenter;
        this.authority = authority;
    }
}
