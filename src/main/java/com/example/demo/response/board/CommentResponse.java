package com.example.demo.response.board;

import lombok.Builder;
import lombok.Data;

@Data
public class CommentResponse {
    private String content;
    private String writer;
    private String inserted;

    @Builder
    public CommentResponse(String content, String writer, String inserted) {
        this.content = content;
        this.writer = writer;
        this.inserted = inserted;
    }
}
