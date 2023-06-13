package com.example.demo.response.board;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommunityResponseDto {

    private Integer id;
    private String title;
    private String writer;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private String inserted;

    @Builder
    public CommunityResponseDto(Integer id, String title, String writer, Integer viewCount, Integer commentCount, Integer likeCount, String inserted) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.inserted = inserted;
    }
}
