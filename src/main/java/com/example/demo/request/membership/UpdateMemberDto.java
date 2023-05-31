package com.example.demo.request.membership;

import lombok.Builder;
import lombok.Data;

@Data
public class UpdateMemberDto {
    private Integer memberId;
    private Integer centerId;
    private Integer trainerId;

    public UpdateMemberDto() {
    }

    @Builder
    public UpdateMemberDto(Integer memberId, Integer centerId, Integer trainerId) {
        this.memberId = memberId;
        this.centerId = centerId;
        this.trainerId = trainerId;
    }
}
