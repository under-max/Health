package com.example.demo.request.membership;

import lombok.Data;

@Data
public class CreateMembershipRequest {
    private Integer memberId;
    private Integer paymentMonths;
    private Integer remainingPT;
}
