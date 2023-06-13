package com.example.demo.controller;

import com.example.demo.entity.AuthUser;
import com.example.demo.entity.Trainer;
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

    @PostMapping("/responsibleUserList/{id}")
    public List<UserListResponse> responsibleUserList(@PathVariable Integer id) {
        List<UserListResponse> responsibleUser = trainerService.responsibleUserList(id);
        return responsibleUser;
    }

    @PostMapping("/schedule/saveSchedule")
    public void schedulePostList(@RequestBody ScheduleRequest scheduleRequest) {
//        System.out.println("scheduleRequest = " + scheduleRequest);
        trainerService.schedulePostList(scheduleRequest);
    }

    @PostMapping("/schedule/getList")
    public Map<Integer, List<ScheduleResponse>> scheduleGetList(AuthUser authUser) {
        Map<Integer, List<ScheduleResponse>> lists = trainerService.scheduleGetList(authUser.getUserId());
        System.out.println(lists);
        return lists;
    }

    @PostMapping("/schedule/deleteSchedule")
    public void scheduleDelete(@RequestBody ScheduleRequest scheduleRequest) {
//        System.out.println("scheduleRequest = " + scheduleRequest);
        trainerService.scheduleDelete(scheduleRequest);
    }

    // 트레이너 상세 페이지 (***작성중***)
    @GetMapping("/trainer/get/{id}")
    public TrainerDetailResponse getTrainerDetail(@PathVariable Integer id) {
        return trainerService.getTrainerDetail(id);
    }

}
