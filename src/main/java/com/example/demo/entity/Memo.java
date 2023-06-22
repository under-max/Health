package com.example.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Memo {
    private Integer memoId;
    private Integer division;
    private String inserted;
    private String title;
    private String content;
    private Integer memberId;


    public Memo(Integer memoId, Integer division, String inserted, String title, String content, Integer memberId) {
        this.memoId = memoId;
        this.division = division;
        this.inserted = inserted;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
    }
}


