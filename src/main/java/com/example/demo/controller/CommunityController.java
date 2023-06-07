package com.example.demo.controller;

import com.example.demo.response.CommunityResponse;
import com.example.demo.service.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @GetMapping("/community")
    public List<CommunityResponse> getCommunityList() {
        return communityService.getCommunityList();
    }
}
