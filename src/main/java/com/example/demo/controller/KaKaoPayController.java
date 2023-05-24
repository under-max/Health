package com.example.demo.controller;

import com.example.demo.service.KaKaoPayService;
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

    @PostMapping("/test/membership/pay")
    public ResponseEntity<KaKaoPayService.PayReadyResponse> pay(@RequestBody PayRequest payRequest) {
        log.info("KaKaoPayController 호출 request={}", payRequest);
        return kaKaoPayService.kakaoPayReady(payRequest.memberId);
    }

    @GetMapping("/test/kakaoPaySuccess")
    public void paySuccess(@RequestParam("pg_token") String token) {
        log.info("pg_token={}", token);
        kaKaoPayService.kakaoPayApprove(token);
    }

    @Data
    static class PayRequest {
        private Integer memberId;
        private Integer totalPrice;
    }

}
