package com.example.demo.service;

import com.example.demo.request.membership.PayReadyRequest;
import com.example.demo.response.membership.PayApproveResponse;
import com.example.demo.response.membership.PayReadyResponse;
import com.example.demo.response.membership.PaySuccessResponse;
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
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class KaKaoPayService {

    @Value("${admin.key}")
    private String adminKey;
    private static final String HOST = "https://kapi.kakao.com";
    public static final String CID = "TC0ONETIME";
    private static final Map<String, String> store = new HashMap<>();

    public PayReadyResponse kakaoPayReady(Long memberId, PayReadyRequest request) {

        URI uri = UriComponentsBuilder
                .fromUriString(HOST)
                .path("/v1/payment/ready")
                .encode()
                .build()
                .toUri();

        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", adminKey);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // item_name
        if (request.getPt() == null) {
            request.setPt(0);
        }

        String itemName = request.getCenterName() + ": 이용권(" + request.getMonth() + "개월, " + "PT " + request.getPt() + "회)";

        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", CID);
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", String.valueOf(memberId));
        params.add("item_name", itemName);
        params.add("quantity", "1");
        params.add("total_amount", String.valueOf(request.getTotalPrice()));
        params.add("tax_free_amount", "10");
        params.add("approval_url", "http://localhost:5173/payment/success"); // 결제 성공 url
        params.add("cancel_url", "http://localhost:5173/payment/cancel"); // 결제 취소 url
        params.add("fail_url", "http://localhost:5173/payment/cancel"); // 결제 실패 url - 결제 취소와 같이 사용 (원래는 실패에 맞는 페이지 작성)
//        params.add("fail_url", "http://localhost:5173/payment/fail"); // 결제 실패 url - 결제 취소와 같이 사용 (원래는 실패에 맞는 페이지 작성)

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(params);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PayReadyResponse> response = restTemplate.exchange(requestEntity, PayReadyResponse.class);

        // approve에서 사용할 정보
        store.put("tid", response.getBody().getTid());

        return response.getBody();
    }

    public PaySuccessResponse kakaoPayApprove(Long memberId, String token) {

        URI uri = UriComponentsBuilder
                .fromUriString(HOST)
                .path("/v1/payment/approve")
                .encode()
                .build()
                .toUri();

        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", adminKey);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", CID);
        params.add("tid", store.get("tid"));
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", String.valueOf(memberId));
        params.add("pg_token", token);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(params);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PayApproveResponse> response = restTemplate.exchange(requestEntity, PayApproveResponse.class);

        //String itemName = request.getCenterName() + ": 이용권(" + request.getMonth() + "개월, " + "PT " + request.getPt() + "회";
        //강백호짐: 이용권(5개월, PT 10회)
        String responseItemName = response.getBody().getItem_name();

        String centerName = responseItemName.substring(0, responseItemName.indexOf(":")).trim();
        String month = responseItemName.substring(responseItemName.indexOf("(") + 1, responseItemName.indexOf("개월")).trim();
        String pt = responseItemName.substring(responseItemName.indexOf("PT") + 3, responseItemName.indexOf("회")).trim();

        PaySuccessResponse successResponse = PaySuccessResponse.builder()
                .memberId(Integer.valueOf(response.getBody().getPartner_user_id()))
                .centerName(centerName)
                .paymentMonths(Integer.valueOf(month))
                .remainingPT(Integer.valueOf(pt))
                .totalPrice(Integer.valueOf(response.getBody().getAmount().getTotal()))
                .approvedDate(response.getBody().getApproved_at().toLocalDate().toString())
                .approvedTime(response.getBody().getApproved_at().toLocalTime().toString())
                .build();

        return successResponse;
    }
}