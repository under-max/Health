package com.example.demo.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
public class Community {

    private Integer id;
    private Grade grade;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime inserted;
    private List<String> fileName;

    @Builder
    public Community(String title, String content, String writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

    public enum Grade {
        NOTICE, FREE
    }

}
