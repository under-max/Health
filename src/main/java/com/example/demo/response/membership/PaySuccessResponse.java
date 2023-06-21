package com.example.demo.response.membership;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PaySuccessResponse {

    private Integer memberId;
    private String centerName;
    private Integer paymentMonths;
    private Integer remainingPT;
    private Integer totalPrice;
    private String approvedDate;
    private String approvedTime;

    @Builder
    public PaySuccessResponse(Integer memberId, String centerName, Integer paymentMonths, Integer remainingPT, Integer totalPrice, String approvedDate, String approvedTime) {
        this.memberId = memberId;
        this.centerName = centerName;
        this.paymentMonths = paymentMonths;
        this.remainingPT = remainingPT;
        this.totalPrice = totalPrice;
        this.approvedDate = approvedDate;
        this.approvedTime = approvedTime;
    }
}
