package com.example.demo.request.membership;

import lombok.Builder;
import lombok.Data;

@Data
public class SimpleCenter {
    private Integer centerId;
    private String centerName;

    public SimpleCenter() {
    }

    @Builder
    public SimpleCenter(Integer centerId, String centerName) {
        this.centerId = centerId;
        this.centerName = centerName;
    }
}
