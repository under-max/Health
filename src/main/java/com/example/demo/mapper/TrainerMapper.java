package com.example.demo.mapper;

import com.example.demo.entity.Trainer;
import com.example.demo.entity.User;
import com.example.demo.response.TrainerDetailResponse;
import com.example.demo.response.UserListResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TrainerMapper {

    @Select("""
            SELECT id, name, address, email, password, 
            phoneNumber, centerId, info, isInCenter
            FROM TRAINER;
            """)
    List<Trainer> selectAllTrainer();

    @Select("""
            SELECT m.id, m.name, ms.remainingPT
            FROM `MEMBER` m
            LEFT JOIN
            	TRAINER t ON m.trainerId = t.id
            LEFT JOIN
            	MEMBERSHIP ms ON m.id = ms.memberId
            WHERE t.id = #{id};
            """)
    List<UserListResponse> selectResponsibleUserList(Integer id);
}
