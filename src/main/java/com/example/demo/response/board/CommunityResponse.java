package com.example.demo.response.board;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommunityResponse<T> {

    private BoardPageInfo pageInfo;
    private T list;

    @Builder
    public CommunityResponse(BoardPageInfo pageInfo, T list) {
        this.pageInfo = pageInfo;
        this.list = list;
    }
}
