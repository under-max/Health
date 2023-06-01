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
        log.info("type={}, keyword={}", type, keyword);

        List<SimpleCenter> centerSearch = membershipService.getCenters(type, keyword);

        log.info("centerSearch={}", centerSearch);
        return centerSearch;
    }

    @GetMapping("/centers/{centerId}")
    public List<SimpleTrainer> getTrainers(@PathVariable Integer centerId) {
        return membershipService.getTrainers(centerId);
    }

}
