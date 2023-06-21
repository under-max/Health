package com.example.demo.response.membership;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SimpleTrainer {

    private Integer trainerId;
    private String trainerName;

    @Builder
    public SimpleTrainer(Integer trainerId, String trainerName) {
        this.trainerId = trainerId;
        this.trainerName = trainerName;
    }
}
