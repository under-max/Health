package com.example.demo.request.comment;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateCommentDto {

    private Integer boardId;
    private String writer;
    private String content;

    @Builder
    public CreateCommentDto(Integer boardId, String writer, String content) {
        this.boardId = boardId;
        this.writer = writer;
        this.content = content;
    }
}
