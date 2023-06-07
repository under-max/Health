package com.example.demo.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommunityResponse {

    private Integer id;
    private String title;
    private String writer;
    private LocalDateTime inserted;

    @Builder
    public CommunityResponse(Integer id, String title, String writer, LocalDateTime inserted) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.inserted = inserted;
    }
}
