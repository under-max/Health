package com.example.demo.mapper;

import com.example.demo.entity.Center;
import com.example.demo.entity.Membership;
import com.example.demo.entity.Trainer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MembershipMapper {

    Integer createMembership(Membership membership);

    Optional<Membership> findByMembershipId(Integer membershipId);

    Membership findByMemberId(Integer memberId);

    Integer updateMembership(Membership membership);

    List<Center> findCenters();

    List<Trainer> findTrainers(Integer centerId);
}
