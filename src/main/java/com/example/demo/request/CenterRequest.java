package com.example.demo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data

public class CenterRequest {

    @NotBlank(message = "이름이 없습니다")
    private String name;

    @NotBlank(message = "주소가 없습니다")
    private String address;

    @NotBlank(message = "매장정보가 없습니다")
    private String info;

    @NotBlank(message = "전화번호가 없습니다")
    private String phoneNumber;

    private String file;

    @Builder
    public CenterRequest(String name, String address, String info, String phoneNumber, String file){

        this.name = name;
        this.address = address;
        this.info = info;
        this.phoneNumber = phoneNumber;
        this.file = file;
    }
}
