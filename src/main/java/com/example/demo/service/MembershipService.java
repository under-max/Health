package com.example.demo.service;

import com.example.demo.entity.Membership;
import com.example.demo.mapper.MembershipMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MembershipService {

    private final MembershipMapper membershipMapper;

    public Membership createMembership(Membership membership) {
        membershipMapper.createMembership(membership);
        return membership;
    }

    public List<Membership> findMemberships() {
        return membershipMapper.findAll();
    }
}
