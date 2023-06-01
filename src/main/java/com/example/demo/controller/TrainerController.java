package com.example.demo.controller;

import com.example.demo.entity.AuthUser;
import com.example.demo.entity.Trainer;
import com.example.demo.entity.User;
import com.example.demo.response.TrainerDetailResponse;
import com.example.demo.response.UserListResponse;
import com.example.demo.service.TrainerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class TrainerController {

    @Autowired
    TrainerService trainerService;

    @GetMapping("/authTrainer")
    public List<TrainerDetailResponse> getTrainer(AuthUser authUser) {
        return trainerService.getAuthTrainer(authUser.getUserId());
    }

    @GetMapping("/test/trainer/{id}")
    public TrainerDetailResponse trainerDetail(@PathVariable Integer id) {
        TrainerDetailResponse trainerDetailRes = trainerService.getTrainerTest();
        return trainerDetailRes;
    }

    @GetMapping("/responsibleUserList/{id}")
    public List<UserListResponse> responsibleUserList(@PathVariable Integer id) {
        List<UserListResponse> responsibleUser = trainerService.responsibleUserList(id);
        return responsibleUser;
    }
}
