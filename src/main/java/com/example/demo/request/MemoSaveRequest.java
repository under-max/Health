package com.example.demo.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemoSaveRequest {
    private Integer division;
    private String title;
    private String content;
    private Integer memberId;

    public MemoSaveRequest(Integer division, String title, String content, Integer memberId) {
        this.division = division;
        this.title = title;
        this.content = content;
        this.memberId = memberId;
    }
}

