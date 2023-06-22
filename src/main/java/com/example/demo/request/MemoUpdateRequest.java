package com.example.demo.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemoUpdateRequest {
    private Integer memoId;
    private String title;
    private String content;

    public MemoUpdateRequest(Integer memoId, String title, String content) {
        this.memoId = memoId;
        this.title = title;
        this.content = content;
    }
}
