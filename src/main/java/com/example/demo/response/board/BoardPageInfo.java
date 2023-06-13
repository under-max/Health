package com.example.demo.response.board;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardPageInfo {

    private Integer leftPageNumber;
    private Integer rightPageNumber;
    private Integer page;
    private Integer lastPageNumber;
    private Integer records;

    @Builder
    public BoardPageInfo(Integer leftPageNumber, Integer rightPageNumber, Integer page, Integer lastPageNumber, Integer records) {
        this.leftPageNumber = leftPageNumber;
        this.rightPageNumber = rightPageNumber;
        this.page = page;
        this.lastPageNumber = lastPageNumber;
        this.records = records;
    }
}
