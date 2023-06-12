package com.example.demo.request.comment;

import lombok.Data;

/**
 * 등록에는 content
 * 수정에는 commentId, content
 * 가 필요하다.
 * 지금은 하나의 dto로 두 곳에서 (add, modify) 사용하지만 나중에 기능이 많아지면 분리
 */
@Data
public class CommentRequest {
    private Integer commentId;
    private String content;
}
