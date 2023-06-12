package com.example.demo.response.board;

import lombok.Builder;
import lombok.Data;

@Data
public class BoardResponse {
    private String title;
    private String content;
    private String writer;
    private Integer viewCount;
    private Integer likeCount;
    private String inserted;

    @Builder
    public BoardResponse(String title, String content, String writer, Integer viewCount, Integer likeCount, String inserted) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.inserted = inserted;
    }
}