package com.example.demo.controller;

import com.example.demo.entity.AuthUser;
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

    @PostMapping
    public ResponseEntity<MembershipResponse> createMembership(@RequestBody CreateMembershipRequest request) {

        log.info("createMembership() request={}", request);

        // 멤버쉽이 없으면 등록, 멤버쉽이 있으면 수정
        MembershipResponse response = membershipService.createAndUpdate(request);

        log.info("createMembership() response={}", response);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 검색 기능
     */
    @GetMapping("/centers")
    public List<SimpleCenter> getCenters(@RequestParam String type, @RequestParam String keyword) {
        log.info("검색 -> type={}, keyword={}", type, keyword);

        List<SimpleCenter> centers = membershipService.getCenters(type, keyword);

        log.info("centerSearch={}", centers);
        return centers;
    }

    @GetMapping("/centers/{centerId}")
    public List<SimpleTrainer> getTrainers(@PathVariable Integer centerId) {
        return membershipService.getTrainers(centerId);
    }

    /**
     * 남은 PT 횟수 가져오기
     */
    @GetMapping("/remainingPT")
    public Integer getRemainingPT(AuthUser authUser) {
        return membershipService.getRemainingPT(authUser.getUserId());
    }

}
