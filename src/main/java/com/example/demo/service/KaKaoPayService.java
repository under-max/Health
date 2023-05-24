package com.example.demo.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Slf4j
@Service
public class KaKaoPayService {

    @Value("${admin.key}")
    private String adminKey;
    private static final String HOST = "https://kapi.kakao.com";
    private static final Map<String, String> store = new HashMap<>();

    public ResponseEntity<PayReadyResponse> kakaoPayReady(Integer memberId) {

        store.put("memberId", String.valueOf(memberId));

        URI uri = UriComponentsBuilder
                .fromUriString(HOST)
                .path("/v1/payment/ready")
                .encode()
                .build()
                .toUri();

        log.info("uri={}", uri);

        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", adminKey);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", String.valueOf(memberId));
        params.add("item_name", "이용권");
        params.add("quantity", "1");
        params.add("total_amount", "100");
        params.add("tax_free_amount", "10");
        params.add("approval_url", "http://localhost:8090/test/kakaoPaySuccess"); // 결제 성공 url
        params.add("cancel_url", "http://localhost:8090/test/kakaoPayCancel"); // 결제 취소 url
        params.add("fail_url", "http://localhost:8090/test/kakaoPaySuccessFail"); // 결제 실패 url

        RequestEntity<MultiValueMap<String, String>> request = RequestEntity
                .post(uri)
                .headers(headers)
                .body(params);

        log.info("kakaoPayReady map request={}", request);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PayReadyResponse> response = restTemplate.exchange(request, PayReadyResponse.class);
        store.put("tid", response.getBody().getTid());
        log.info("Headers={}", response.getHeaders());
        log.info("StatusCode={}", response.getStatusCode());
        log.info("body={}", response.getBody());

        return response;
    }

    public ResponseEntity<PayApproveResponse> kakaoPayApprove(String pgToken) {

        URI uri = UriComponentsBuilder
                .fromUriString(HOST)
                .path("/v1/payment/approve")
                .encode()
                .build()
                .toUri();

        log.info("uri={}", uri);

        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", adminKey);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //?
//        ResponseEntity<PayReadyResponse> ready = kakaoPayReady(1);

        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", store.get("tid"));
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", store.get("memberId"));
        params.add("pg_token", pgToken);

        RequestEntity<MultiValueMap<String, String>> request = RequestEntity
                .post(uri)
                .headers(headers)
                .body(params);

        log.info("kakaoPayApprove map request={}", request);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PayApproveResponse> response = restTemplate.exchange(request, PayApproveResponse.class);
        log.info("Headers={}", response.getHeaders());
        log.info("StatusCode={}", response.getStatusCode());
        log.info("body={}", response.getBody());

        return response;
    }

    @Data
    public static class PayReadyResponse {
        private String tid;
        private String next_redirect_pc_url;
        private LocalDateTime create_at;
    }

    @Data
    public static class PayApproveResponse {
        private String aid;
        private String tid;
        private String cid;
        private String sid;
        private String partner_order_id;
        private String partner_user_id;
        private String payment_method_type;
        private Amount amount;
        private CardInfo card_info;
        private String item_name;
        private String item_code;
        private Integer quantity;
        private LocalDateTime created_at;
        private LocalDateTime approved_at;
        private String payload;
    }

    @Data
    static class Amount {
        private String total;
        private String tax_free;
        private String vat;
        private String point;
        private String discount;
        private String green_deposit;
    }

    @Data
    static class CardInfo {
        private String purchase_corp;
        private String purchase_corp_code;
        private String issuer_corp;
        private String issuer_corp_code;
        private String kakaopay_purchase_corp;
        private String kakaopay_purchase_corp_code;
        private String kakaopay_issuer_corp;
        private String kakaopay_issuer_corp_code;
        private String bin;
        private String card_type;
        private String install_month;
        private String approved_id;
        private String card_mid;
        private String interest_free_install;
        private String card_item_code;
    }
}