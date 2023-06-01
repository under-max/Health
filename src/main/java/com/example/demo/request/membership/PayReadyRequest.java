package com.example.demo.request.membership;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PayReadyRequest {
//    private Integer memberId;
    private Integer centerId;

    @NotEmpty(message = "센터를 선택해주세요!")
    private String centerName;
    private Integer month;
    private Integer pt;
    private Integer totalPrice;
}
