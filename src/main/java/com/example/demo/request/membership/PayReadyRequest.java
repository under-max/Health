package com.example.demo.request.membership;

import lombok.Data;

@Data
public class PayReadyRequest {

    private Integer centerId;
    private String centerName;
    private Integer month;
    private Integer pt;
    private Integer totalPrice;
}
