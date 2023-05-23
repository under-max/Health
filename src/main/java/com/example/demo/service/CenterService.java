package com.example.demo.service;

import com.example.demo.entity.CenterEntity;
import com.example.demo.mapper.CenterMapper;
import com.example.demo.request.CenterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)

public class CenterService {

    private final CenterMapper centerMapper;

    @Autowired
    public CenterService(CenterMapper centerMapper){
        this.centerMapper = centerMapper;
    }
    public void storeRegi(CenterRequest centerRequest) {

        CenterEntity centerEntity = CenterEntity
                .builder()
                .name(centerRequest.getName())
                .address(centerRequest.getAddress())
                .info(centerRequest.getInfo())
                .phoneNumber(centerRequest.getPhoneNumber())
                .file(centerRequest.getFile())
                .build();

        centerMapper.storeRegi(centerEntity);
    }
}
