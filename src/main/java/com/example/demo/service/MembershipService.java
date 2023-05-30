package com.example.demo.service;

import com.example.demo.entity.Center;
import com.example.demo.entity.Membership;
import com.example.demo.entity.Trainer;
import com.example.demo.mapper.MembershipMapper;
import com.example.demo.request.membership.CreateMembershipRequest;
import com.example.demo.request.membership.SimpleCenter;
import com.example.demo.request.membership.SimpleTrainer;
import com.example.demo.response.membership.MembershipResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MembershipService {

    private final MembershipMapper membershipMapper;

    public Integer createMembership(CreateMembershipRequest request) {

        LocalDate start = LocalDate.now();
        LocalDate end = start.plusMonths(request.getPaymentMonths());

        Membership membership = Membership.builder()
                .memberId(request.getMemberId())
                .startDate(start)
                .endDate(end)
                .remainingPT(request.getRemainingPT())
                .build();

        membershipMapper.createMembership(membership);

        return membership.getMembershipId();
    }

    public MembershipResponse getMembershipByMembershipId(Integer membershipId) {
        Membership findMembership = membershipMapper.findByMembershipId(membershipId)
                .orElseThrow(() -> new IllegalArgumentException("존재하는 멤버쉽이 없습니다."));

        MembershipResponse response = MembershipResponse.builder()
                .startDate(findMembership.getStartDate())
                .endDate(findMembership.getEndDate())
                .remainingPT(findMembership.getRemainingPT())
                .build();

        return response;
    }

    public Membership getMembershipByMemberId(Integer memberId) {
        return membershipMapper.findByMemberId(memberId);
    }

    public Integer updateMembership(CreateMembershipRequest request) {

        Membership findMembership = getMembershipByMemberId(request.getMemberId());

        LocalDate start = findMembership.getEndDate();
        LocalDate addDate = start.plusMonths(request.getPaymentMonths());

        Integer pastRemainingPT = findMembership.getRemainingPT();
        Integer addRemainingPT = pastRemainingPT + request.getRemainingPT();

        Membership membership = Membership.builder()
                .memberId(request.getMemberId())
                .endDate(addDate)
                .remainingPT(addRemainingPT)
                .build();

        membershipMapper.updateMembership(membership);

        return findMembership.getMembershipId();
    }

    public List<SimpleCenter> getCenters() {
        return membershipMapper.findCenters().stream()
                .map(center -> SimpleCenter.builder()
                        .centerId(center.getId())
                        .centerName(center.getName())
                        .build())
                .collect(Collectors.toList());

    }

    public List<SimpleTrainer> getTrainers(Integer centerId) {
        return membershipMapper.findTrainers(centerId).stream()
                .map(trainer -> SimpleTrainer.builder()
                        .trainerId(trainer.getId())
                        .trainerName(trainer.getName())
                        .build())
                .collect(Collectors.toList());
    }
}
