package com.example.demo.service;

import com.example.demo.entity.Trainer;
import com.example.demo.mapper.TrainerMapper;
import com.example.demo.response.TrainerDetailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TrainerService {

    @Autowired
    private TrainerMapper trainerMapper;


    public List<Trainer> listAll() {
        return trainerMapper.selectAllTrainer();
    }

    public TrainerDetailResponse getTrainerTest() {
        return null;
    }
}
