package com.example.demo.mapper;

import com.example.demo.entity.Membership;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MembershipMapper {

    void createMembership(Membership membership);

    List<Membership> findAll();

    Optional<Membership> findById(Integer memberId);

    Integer update(Membership membership);

}
