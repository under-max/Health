package com.example.demo.service;

import com.example.demo.entity.Community;
import com.example.demo.mapper.CommunityMapper;
import com.example.demo.response.CommunityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityMapper communityMapper;

    public List<CommunityResponse> getCommunityList() {
        return communityMapper.findAll().stream()
                .map(community -> CommunityResponse.builder()
                        .id(community.getId())
                        .title(community.getTitle())
                        .writer(community.getWriter())
                        .inserted(community.getInserted()).
                        build()).collect(Collectors.toList());
    }
}
