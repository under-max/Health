package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CenterComment {

    private Long id;

    private Long star;

    private String comment;

    private Long memberId;

    private Long centerId;

    @JsonFormat(pattern = "yyyy-MM-dd-HH:mm:ss")
    private LocalDateTime inserted;

    private String nickName;

    @Builder
    public CenterComment(Long id, Long star, String comment, Long memberId, LocalDateTime inserted, String nickName, Long centerId){
        this.id = id;
        this.star = star;
        this.comment = comment;
        this.memberId = memberId;
        this.inserted = inserted;
        this.nickName = nickName;
        this.centerId = centerId;
    }
}
