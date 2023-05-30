package com.example.demo.controller;

import com.example.demo.entity.AuthUser;
import com.example.demo.entity.Trainer;
import com.example.demo.response.TrainerDetailResponse;
import com.example.demo.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TrainerController {

    @Autowired
    TrainerService trainerService;

    @GetMapping("/test/trainer/list")
    public List<Trainer> listAll(Integer id) {
        List<Trainer> trainerlistAll = trainerService.listAll();
        return trainerlistAll;
    }

    @GetMapping("/test/trainer/{id}")
    public TrainerDetailResponse trainerDetail(@PathVariable Integer id) {
        TrainerDetailResponse trainerDetailRes = trainerService.getTrainerTest();
        return trainerDetailRes;
    }
}
