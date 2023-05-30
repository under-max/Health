package com.example.demo.request.membership;

import lombok.Builder;
import lombok.Data;

@Data
public class SimpleTrainer {
    private Integer trainerId;
    private String trainerName;

    public SimpleTrainer() {
    }

    @Builder
    public SimpleTrainer(Integer trainerId, String trainerName) {
        this.trainerId = trainerId;
        this.trainerName = trainerName;
    }
}
