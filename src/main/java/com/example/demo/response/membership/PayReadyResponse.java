package com.example.demo.response.membership;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PayReadyResponse {

    private String tid;
    private String next_redirect_pc_url;
    private LocalDateTime created_at;
}
