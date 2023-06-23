package com.example.demo.response;

import lombok.Data;
import lombok.NoArgsConstructor;

// 수정
@Data
@NoArgsConstructor
public class CenterDetailResponse {
    private Integer id;
    private String name;
    private String address;
    private String info;
    private Double star;
    private String phoneNumber;

    public CenterDetailResponse(Integer id, String name, String address, String info, Double star, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.info = info;
        this.star = star;
        this.phoneNumber = phoneNumber;
    }
}
