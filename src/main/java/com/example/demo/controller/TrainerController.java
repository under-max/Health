package com.example.demo.controller;

import com.example.demo.entity.AuthUser;
import com.example.demo.entity.Schedule;
import com.example.demo.request.schedule.DateRequest;
import com.example.demo.request.schedule.ScheduleRequest;
import com.example.demo.request.schedule.ScheduleUpdateRequest;
import com.example.demo.response.ScheduleResponse;
import com.example.demo.response.TrainerDetailResponse;
import com.example.demo.response.UserListResponse;
import com.example.demo.service.TrainerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class TrainerController {

    @Autowired
    TrainerService trainerService;

    @GetMapping("/authTrainer")
    public List<TrainerDetailResponse> getTrainer(AuthUser authUser) {
        return trainerService.getAuthTrainer(authUser.getUserId());
    }

    @PostMapping("/responsibleUserList/{id}")
    public List<UserListResponse> responsibleUserList(@PathVariable Integer id) {
        List<UserListResponse> responsibleUser = trainerService.responsibleUserList(id);
        return responsibleUser;
    }

    @PostMapping("/schedule/saveSchedule")
    public void schedulePostList(@RequestBody ScheduleRequest scheduleRequest) {
        trainerService.schedulePostList(scheduleRequest);
    }

    @GetMapping("/schedule/getList")
    public Map<Integer, List<ScheduleResponse>> scheduleGetList(AuthUser authUser, @ModelAttribute DateRequest dateRequest) {
        Map<Integer, List<ScheduleResponse>> lists = trainerService.scheduleGetList(authUser.getUserId(), dateRequest);
        return lists;
    }

    @PostMapping("/schedule/deleteSchedule")
    public void scheduleDelete(@RequestBody ScheduleRequest scheduleRequest) {
        trainerService.scheduleDelete(scheduleRequest);
    }

    // 고객으로 로그인했을때 트레이너 상세 정보 확인
    @GetMapping("/trainer/get/{id}")
    public TrainerDetailResponse getTrainerDetail(@PathVariable Integer id) {
        return trainerService.getTrainerDetail(id);
    }

    @PostMapping("/schedule/updateSchedule")
    public void scheduleUpdate(@RequestBody ScheduleUpdateRequest scheduleUpdateRequest) {
        trainerService.scheduleUpdate(scheduleUpdateRequest);
    }

}
