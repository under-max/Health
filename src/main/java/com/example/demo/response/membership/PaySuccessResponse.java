package com.example.demo.response.membership;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaySuccessResponse {
    private Integer memberId;
    private String itemName;
    private Integer paymentMonths;
    private Integer remainingPT;
    private Integer totalPrice;
    private LocalDateTime approvedAt;

    public PaySuccessResponse() {
    }

    @Builder
    public PaySuccessResponse(Integer memberId, String itemName, Integer paymentMonths, Integer remainingPT, Integer totalPrice, LocalDateTime approvedAt) {
        this.memberId = memberId;
        this.itemName = itemName;
        this.paymentMonths = paymentMonths;
        this.remainingPT = remainingPT;
        this.totalPrice = totalPrice;
        this.approvedAt = approvedAt;
    }
}
