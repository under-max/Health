package com.example.demo.controller;

import com.example.demo.request.membership.CreateMembershipRequest;
import com.example.demo.request.membership.SimpleCenter;
import com.example.demo.request.membership.SimpleTrainer;
import com.example.demo.response.membership.MembershipResponse;
import com.example.demo.service.MembershipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/membership")
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @PostMapping()
    public ResponseEntity<MembershipResponse> createMembership(@RequestBody CreateMembershipRequest request) {

        log.info("createMembership() request={}", request);

        Integer membershipId;
        if (membershipService.getMembershipByMemberId(request.getMemberId()) == null) {
            membershipId = membershipService.createMembership(request);
        } else {
            membershipId = membershipService.updateMembership(request);
        }

        MembershipResponse response = membershipService.getMembershipByMembershipId(membershipId);

        log.info("createMembership() response={}", response);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/centers")
    public List<SimpleCenter> getCenters() {
        return membershipService.getCenters();
    }

    @GetMapping("/centers/{centerId}")
    public List<SimpleTrainer> getTrainers(@PathVariable Integer centerId) {
        log.info("getTrainers={}", membershipService.getTrainers(centerId));
        return membershipService.getTrainers(centerId);
    }

}
