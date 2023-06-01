package com.example.demo.service;

import com.example.demo.entity.Trainer;
import com.example.demo.entity.User;
import com.example.demo.exception.UserNotFound;
import com.example.demo.mapper.TrainerMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.response.TrainerDetailResponse;
import com.example.demo.response.UserListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TrainerService {

    @Autowired
    private TrainerMapper trainerMapper;

    @Autowired
    private UserMapper userMapper;



    public List<TrainerDetailResponse> getAuthTrainer(Long id) {
        if (userMapper.findById(id).orElseThrow(UserNotFound::new).getAuthority()==1){
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
}
