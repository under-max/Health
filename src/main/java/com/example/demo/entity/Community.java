package com.example.demo.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
public class Community {

    private Integer id;
    private String grade;
    private String title;
    private String content;
    private String writer;
    private Integer viewCount;
    private Integer likeCount;
    private LocalDateTime inserted;

    public Community() {
    }

    @Builder
    public Community(String title, String grade, String content, String writer) {
        this.title = title;
        this.grade = grade;
        this.content = content;
        this.writer = writer;
    }

}
