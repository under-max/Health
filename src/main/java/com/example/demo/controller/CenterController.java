package com.example.demo.controller;

import com.example.demo.request.CenterRequest;
import com.example.demo.service.CenterService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CenterController {


    private final CenterService centerService;

    @Autowired
    public CenterController(CenterService centerService){
        this.centerService = centerService;
    }
    @PostMapping("search/input")
    public String storeRegi(@RequestBody @Valid CenterRequest centerRequest){

        centerService.storeRegi(centerRequest);
        log.info("{}",centerRequest);
        return "ok";
    }
}
