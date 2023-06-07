package com.example.demo.mapper;

import com.example.demo.entity.Community;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunityMapper {

    List<Community> findAll();
}
