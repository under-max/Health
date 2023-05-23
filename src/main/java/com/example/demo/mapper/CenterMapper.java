package com.example.demo.mapper;

import com.example.demo.entity.CenterEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CenterMapper {


    void storeRegi(CenterEntity centerEntity);
}
