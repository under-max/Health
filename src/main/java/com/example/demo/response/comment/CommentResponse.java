package com.example.demo.response.comment;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentResponse {
    private Integer id;
    private String writer;
    private String content;
    private String inserted;

    @Builder
    public CommentResponse(Integer id, String writer, String content, String inserted) {
        this.id = id;
        this.writer = writer;
        this.content = content;
        this.inserted = inserted;
    }
}
