package com.example.demo.mapper;

import com.example.demo.entity.Trainer;
import com.example.demo.response.TrainerDetailResponse;
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

}
