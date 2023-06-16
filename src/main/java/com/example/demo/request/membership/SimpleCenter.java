package com.example.demo.request.membership;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SimpleCenter {
    private Integer centerId;
    private String centerName;

    @Builder
    public SimpleCenter(Integer centerId, String centerName) {
        this.centerId = centerId;
        this.centerName = centerName;
    }
}
