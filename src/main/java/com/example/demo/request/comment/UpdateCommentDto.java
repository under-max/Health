package com.example.demo.request.comment;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateCommentDto {

    private Integer boardId;
    private Integer commentId;
    private String writer;
    private String content;

    @Builder
    public UpdateCommentDto(Integer boardId, Integer commentId, String writer, String content) {
        this.boardId = boardId;
        this.commentId = commentId;
        this.writer = writer;
        this.content = content;
    }
}
