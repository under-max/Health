package com.example.demo.controller;

import com.example.demo.request.membership.PayReadyRequest;
import com.example.demo.response.membership.PaySuccessResponse;
import com.example.demo.service.KaKaoPayService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KaKaoPayController {

    private final KaKaoPayService kaKaoPayService;

    @PostMapping("/kakaopay")
    public String payReady(@RequestBody @Valid PayReadyRequest request) {

        log.info("payReady() request={}", request);
        String redirectPcUrl = kaKaoPayService.kakaoPayReady(request).getBody().getNext_redirect_pc_url();
        return redirectPcUrl;
    }

    @GetMapping("/kakaopay/{pg_token}")
    public PaySuccessResponse payApprove(@PathVariable("pg_token") String token) {
        return kaKaoPayService.kakaoPayApprove(token);
    }

}
