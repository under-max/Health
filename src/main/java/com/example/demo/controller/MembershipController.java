package com.example.demo.controller;

import com.example.demo.entity.Membership;
import com.example.demo.service.MembershipService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping("/test/membership/create")
    public ResponseEntity<MembershipResponse> createMemberShip(@RequestBody MemberShipRequest request) {

        log.info("createMemberShip 호출, request={}", request);


        LocalDate start = LocalDate.now();
        LocalDate end = start.plusMonths(request.paymentMonths);

        Membership membership = Membership.builder()
                .memberId(request.memberId)
                .startDate(start)
                .endDate(end)
                .remainingPT(request.remainingPT)
                .build();

        Membership createdMembership = membershipService.createMembership(membership);

        MembershipResponse response = MembershipResponse.builder()
                .membershipId(createdMembership.getMembershipId())
                .memberId(createdMembership.getMemberId())
                .startDate(createdMembership.getStartDate())
                .endDate(createdMembership.getEndDate())
                .remainingPT(createdMembership.getRemainingPT())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/test/membership/items")
    public List<MembershipResponse> findMemberships() {
        List<Membership> memberships = membershipService.findMemberships();
        List<MembershipResponse> responseList = memberships.stream().map(m -> MembershipResponse.builder()
                        .membershipId(m.getMembershipId())
                        .memberId(m.getMemberId())
                        .startDate(m.getStartDate())
                        .endDate(m.getEndDate())
                        .remainingPT(m.getRemainingPT())
                        .build())
                .collect(Collectors.toList());
        return responseList;
    }

    @PutMapping("/test/membership/{id}/update")
    public MembershipResponse updateMembership(@PathVariable("id") Integer memberId, @RequestBody MembershipUpdateRequest request) {

        log.info("request={}", request);

        Membership findMembership = membershipService.findMembership(memberId);
        LocalDate start = findMembership.getEndDate();
        LocalDate addDate = start.plusMonths(request.getAddPaymentMonths());

        Integer pastRemainingPT = findMembership.getRemainingPT();
        Integer addRemainingPT = pastRemainingPT + request.getRemainingPT();

        Membership membership = Membership.builder()
                .memberId(request.memberId)
                .endDate(addDate)
                .remainingPT(addRemainingPT)
                .build();

        membershipService.update(membership);
        Membership updateMembership = membershipService.findMembership(request.memberId);

        log.info("updateMembership={}", updateMembership);

        MembershipResponse response = MembershipResponse.builder()
                .membershipId(updateMembership.getMembershipId())
                .memberId(updateMembership.getMemberId())
                .startDate(updateMembership.getStartDate())
                .endDate(updateMembership.getEndDate())
                .remainingPT(updateMembership.getRemainingPT())
                .build();

        return response;
    }

    @Data
    static class MemberShipRequest {
        private Integer memberId;
        private Integer paymentMonths;
        private Integer remainingPT;
        private Integer totalPrice;
    }


    @Data
    static class MembershipResponse {

        private Integer membershipId;
        private Integer memberId;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer remainingPT;

        @Builder
        public MembershipResponse(Integer membershipId, Integer memberId, LocalDate startDate, LocalDate endDate, Integer remainingPT) {
            this.membershipId = membershipId;
            this.memberId = memberId;
            this.startDate = startDate;
            this.endDate = endDate;
            this.remainingPT = remainingPT;
        }
    }

    @Data
    public static class MembershipUpdateRequest {
        private Integer memberId;
        private Integer addPaymentMonths;
        private Integer remainingPT;
    }

}
