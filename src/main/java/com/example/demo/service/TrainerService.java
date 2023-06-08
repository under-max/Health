package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.exception.DuplicateSchedule;
import com.example.demo.exception.UserNotFound;
import com.example.demo.mapper.TrainerMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.request.ScheduleRequest;
import com.example.demo.response.ScheduleResponse;
import com.example.demo.response.TrainerDetailResponse;
import com.example.demo.response.UserListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TrainerService {

    @Autowired
    private TrainerMapper trainerMapper;

    @Autowired
    private UserMapper userMapper;


    public List<TrainerDetailResponse> getAuthTrainer(Long id) {
        if (userMapper.findById(id).orElseThrow(UserNotFound::new).getAuthority() == 1) {
            // 손님일 경우 트레이너 정보 출력
            User user = trainerMapper.getTrainerById(id);
            System.out.println("트레이너 : " + user.getTrainerId());
            return trainerMapper.findByAuthTrainerUId(user.getTrainerId());
        } else {
            return trainerMapper.findByAuthTrainerTId(id);

        }
    }

    public TrainerDetailResponse getTrainerTest() {
        return null;
    }

    public List<UserListResponse> responsibleUserList(Integer id) {
        List<UserListResponse> test = trainerMapper.selectResponsibleUserList(id);
        System.out.println(test);
        return test;
    }

    public ScheduleResponse schedulePostList(ScheduleRequest scheduleRequest)  {
        scheduleRequest.setPtTime(scheduleRequest.getPtTime().plusHours(9));
//        if(trainerMapper.insertSchedule(scheduleResponse) == 1) {
//            return trainerMapper.findByMemberId(scheduleResponse.getMemberId(), scheduleResponse.getPtTime());
//        } else {
//            throw new RuntimeException();
//        }
        // Optional 클래스 사용
        try {
            trainerMapper.insertSchedule(scheduleRequest);
        } catch (Exception e) {
            throw new DuplicateSchedule(e);
        }
        ScheduleResponse byMemberId = trainerMapper.findByMemberId(scheduleRequest.getMemberId(), scheduleRequest.getPtTime())
                .orElseThrow(DuplicateSchedule::new);
//              .orElseThrow(() -> new DuplicateSchedule());

        return byMemberId;
    }

    public Map<Integer, List<ScheduleResponse>> scheduleGetList() {

//         ptTime의 년,월 계산
//        ScheduleResponse scheduleResponse = trainerMapper.getPtTime();
//        int month = Integer.parseInt(String.valueOf(scheduleResponse.getPtTime().getMonth()));
//        int year = scheduleResponse.getPtTime().getYear();
//        System.out.println("month : " + month + ", year : " + year );
//        int lastDay = calculateLastDayOfMonth(year, month);
//        System.out.println("Last day of the month: " + lastDay);

        Map<Integer, List<ScheduleResponse>> getLists = new HashMap<>();
        for(int i = 1; i <= 31; i++) {
            getLists.put(i, trainerMapper.findByIdWithRes(i));
        }

        return getLists;
    }

    public static int calculateLastDayOfMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        return yearMonth.lengthOfMonth();
    }
}
