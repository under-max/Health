package com.example.demo.controller;

import com.example.demo.request.CenterRequest;
import com.example.demo.response.CenterResponse;
import com.example.demo.service.CenterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
public class CenterController {


    private final CenterService centerService;

    @Autowired
    public CenterController(CenterService centerService) {
        this.centerService = centerService;
    }

    //create
    @PostMapping(value = "center/infoInsert", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String centerInputImg(@RequestParam("centerImg") MultipartFile[] centerImg,
                                 @RequestParam("name") String name,
                                 @RequestParam("address") String address,
                                 @RequestParam("phoneNumber") String phoneNumber,
                                 @RequestParam("info") String info) throws Exception{

        CenterRequest centerRequest = CenterRequest.builder()
                .name(name)
                .address(address)
                .phoneNumber(phoneNumber)
                .info(info)
                .build();
        System.out.println(centerRequest);
       centerService.infoInsert(centerRequest, centerImg);

        return "이미지 완료";
    }

    //단건 get
    @GetMapping("/center/searchname")
    public List<CenterResponse> CenterSearch(@RequestParam("centerName") String centerName){

        System.out.println(centerName);
        List<CenterResponse> responses = centerService.getByCenterName(centerName);

        return responses;
    }

}
