package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.exception.DuplicateSchedule;
import com.example.demo.exception.UserNotFound;
import com.example.demo.mapper.TrainerMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.request.DateRequest;
import com.example.demo.request.ScheduleRequest;
import com.example.demo.request.ScheduleUpdateRequest;
import com.example.demo.response.ScheduleResponse;
import com.example.demo.response.TrainerDetailResponse;
import com.example.demo.response.UserListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
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

    public List<UserListResponse> responsibleUserList(Integer id) {
        List<UserListResponse> list = trainerMapper.selectResponsibleUserList(id);
        System.out.println("responsibleUserList" + list);
        return list;
    }

    public void schedulePostList(ScheduleRequest scheduleRequest)  {
        scheduleRequest.setPtTime(scheduleRequest.getPtTime().plusHours(9));

        try {
            trainerMapper.insertSchedule(scheduleRequest);
            trainerMapper.updateMembershipPT(scheduleRequest);
        } catch (Exception e) {
            throw new DuplicateSchedule(e);
        }

    }

    public Map<Integer, List<ScheduleResponse>> scheduleGetList(Long id, DateRequest dateRequest) {

        int year = dateRequest.getYear();
        int month = dateRequest.getMonth();

        System.out.println("month : " + month + ", year : " + year );
        int lastDay = calculateLastDayOfMonth(year, month);
        System.out.println("Last day of the month: " + lastDay);

        // List<Schedule>
        Map<Integer, List<ScheduleResponse>> getLists = new HashMap<>();
        for(int day = 1; day <= lastDay; day++) {
            getLists.put(day, trainerMapper.findByIdWithRes(day, year, month, id).stream()
                    .map(s -> ScheduleResponse.builder()
                            .id(s.getId())
                            .memberName(s.getMemberName())
                            .memberId(s.getMemberId())
                            .remainingPT(s.getRemainingPT())
                            .pt(s.getPtTime().toLocalTime().toString())
                            .build()).collect(Collectors.toList()));
        }
        return getLists;
    }

    private int calculateLastDayOfMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        return yearMonth.lengthOfMonth();
    }


    public void scheduleDelete(ScheduleRequest scheduleRequest) {
        trainerMapper.deleteById(scheduleRequest);
        System.out.println("$$#$@!@#!@#" + scheduleRequest);
        trainerMapper.deleteMembershipPT(scheduleRequest);
    }

    public TrainerDetailResponse getTrainerDetail(Integer id) {
        return trainerMapper.getTrainerDetail(id);
    }

    public void scheduleUpdate(ScheduleUpdateRequest scheduleUpdateRequest) {
        scheduleUpdateRequest.setPtHour(scheduleUpdateRequest.getPtHour().plusHours(9));
        System.out.println("scheduleUpdateRequest = " + scheduleUpdateRequest);
        trainerMapper.scheduleUpdate(scheduleUpdateRequest);
    }
}
