package com.example.demo.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class Comment {
    private Integer id;
    private Integer boardId;
    private String content;
    private String writer;
    private LocalDateTime inserted;

    public Comment() {
    }

    @Builder
    public Comment(String content, String writer) {
        this.content = content;
        this.writer = writer;
    }
}
