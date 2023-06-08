package com.example.demo.controller;

import com.example.demo.entity.AuthUser;
import com.example.demo.request.ScheduleRequest;
import com.example.demo.response.ScheduleResponse;
import com.example.demo.response.TrainerDetailResponse;
import com.example.demo.response.UserListResponse;
import com.example.demo.service.TrainerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
public class TrainerController {

    @Autowired
    TrainerService trainerService;

    @GetMapping("/authTrainer")
    public List<TrainerDetailResponse> getTrainer(AuthUser authUser) {
        return trainerService.getAuthTrainer(authUser.getUserId());
    }

//    @GetMapping("/test/trainer/{id}")
//    public TrainerDetailResponse trainerDetail(@PathVariable Integer id) {
//        TrainerDetailResponse trainerDetailRes = trainerService.getTrainerTest();
//        return trainerDetailRes;
//    }

    @GetMapping("/responsibleUserList/{id}")
    public List<UserListResponse> responsibleUserList(@PathVariable Integer id) {
        List<UserListResponse> responsibleUser = trainerService.responsibleUserList(id);
        return responsibleUser;
    }

    @PostMapping("/schedule/list")
    public void schedulePostList(@RequestBody ScheduleRequest scheduleRequest) {
        trainerService.schedulePostList(scheduleRequest);
    }

    @GetMapping("/schedule/list")
    public Map<Integer, List<ScheduleResponse>> scheduleGetList() {
        Map<Integer, List<ScheduleResponse>> lists = trainerService.scheduleGetList();
        return lists;
    }
}
