package com.example.demo.request.comment;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteCommentDto {

    private Integer boardId;
    private Integer commentId;
    private String writer;

    @Builder
    public DeleteCommentDto(Integer boardId, Integer commentId, String writer) {
        this.boardId = boardId;
        this.commentId = commentId;
        this.writer = writer;
    }
}
