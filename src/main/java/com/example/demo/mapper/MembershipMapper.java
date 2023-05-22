package com.example.demo.mapper;

import com.example.demo.entity.Membership;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MembershipMapper {

    void createMembership(Membership membership);

    List<Membership> findAll();
}
