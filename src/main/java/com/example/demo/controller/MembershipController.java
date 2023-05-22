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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MembershipController {

//    private Map<String, String> map = new HashMap<>();
    private final MembershipService membershipService;
//    public MembershipController() {
//        map.put("1", "java");
//        map.put("2", "spring");
//        map.put("3", "vue");
//        map.put("4", "axios");
//    }
//
//    @GetMapping("/test/{id}")
//    public String getItem(@PathVariable String id) {
//        log.info("data={}", map.get(id));
//        return map.get(id);
//    }
//
//    @GetMapping("/test")
//    public List<String> items() {
//        List<String> values = (List<String>) map.values();
//        return values;
//    }

    @PostMapping("/test/create")
    public ResponseEntity<MembershipResponse> createMemberShip(@RequestBody MemberShipRequest request) {
        log.info("createMemberShip 호출");
        Membership membership = Membership.builder()
                .memberId(request.memberId)
                .remainingPT(request.remainingPT)
                .build();
        membership.setStartDate("2023-05-22");
        membership.setEndDate("2023-06-01");

        Membership saveMembership = membershipService.createMembership(membership);


        MembershipResponse response = MembershipResponse.builder()
                .membershipId(saveMembership.getMembershipId())
                .memberId(saveMembership.getMemberId())
                .startDate(saveMembership.getStartDate())
                .endDate(saveMembership.getEndDate())
                .remainingPT(saveMembership.getRemainingPT())
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/test/memberships")
    public List<MembershipResponse> findMemberships() {
        List<Membership> memberships = membershipService.findMemberships();
        List<MembershipResponse> responseList = memberships.stream().map(m -> MembershipResponse.builder()
                .membershipId(m.getMembershipId())
                .memberId(m.getMemberId())
                .startDate(m.getStartDate())
                .endDate(m.getEndDate())
                .remainingPT(m.getRemainingPT())
                .build()
        ).collect(Collectors.toList());
        return responseList;
    }

    @Data
    static class MemberShipRequest {
        private Integer memberId;
        private Integer remainingPT;
    }

    @Data
    static class MembershipResponse {
        private Integer membershipId;
        private Integer memberId;
        private String startDate;
        private String endDate;
        private Integer remainingPT;

        @Builder
        public MembershipResponse(Integer membershipId, Integer memberId, String startDate, String endDate, Integer remainingPT) {
            this.membershipId = membershipId;
            this.memberId = memberId;
            this.startDate = startDate;
            this.endDate = endDate;
            this.remainingPT = remainingPT;
        }
    }
}
