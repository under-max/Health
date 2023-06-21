package com.example.demo.service;

import com.example.demo.entity.Membership;
import com.example.demo.mapper.MembershipMapper;
import com.example.demo.request.membership.CreateMembershipRequest;
import com.example.demo.response.membership.SimpleCenter;
import com.example.demo.response.membership.SimpleTrainer;
import com.example.demo.request.membership.UpdateMemberDto;
import com.example.demo.response.membership.MembershipResponse;
import lombok.*;
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

    public MembershipResponse createAndUpdate(CreateMembershipRequest request) {

        Integer membershipId;
        if (getMembershipByMemberId(request.getMemberId()) == null) {
            membershipId = createMembership(request);

        } else {
            membershipId = updateMembership(request);
        }

        // MEMBER 테이블에 centerId, trainerId 값을 넣어주는 메서드
        updateMember(request);

        return getMembershipByMembershipId(membershipId);
    }

    /**
     * 검색 기능
     */
    public List<SimpleCenter> getCenters(String type, String keyword) {
        return membershipMapper.findCenters(type, keyword).stream()
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

    private Integer createMembership(CreateMembershipRequest request) {

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

    private MembershipResponse getMembershipByMembershipId(Integer membershipId) {

        Membership findMembership = membershipMapper.findByMembershipId(membershipId)
                .orElseThrow(() -> new IllegalArgumentException("존재하는 멤버쉽이 없습니다."));

        MembershipResponse response = MembershipResponse.builder()
                .startDate(findMembership.getStartDate())
                .endDate(findMembership.getEndDate())
                .remainingPT(findMembership.getRemainingPT())
                .build();

        return response;
    }

    private Membership getMembershipByMemberId(Integer memberId) {
        return membershipMapper.findByMemberId(memberId);
    }

    private Integer updateMembership(CreateMembershipRequest request) {

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

    private void updateMember(CreateMembershipRequest request) {
        UpdateMemberDto dto = UpdateMemberDto.builder()
                .memberId(request.getMemberId())
                .centerId(request.getCenterId())
                .trainerId(request.getTrainerId())
                .build();

        membershipMapper.updateMember(dto);
    }


    public Integer getRemainingPT(Long userId) {
        Membership membership = membershipMapper.findByMemberId(userId.intValue());

        if (membership == null) {
            return -1;
        }
        return membership.getRemainingPT();
    }
}
