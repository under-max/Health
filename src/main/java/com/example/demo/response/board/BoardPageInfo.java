package com.example.demo.response.board;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardPageInfo {

    private Integer leftPageNumber;
    private Integer rightPageNumber;
    private Integer currentPageNumber;
    private Integer lastPageNumber;
    private Integer records;

    @Builder
    public BoardPageInfo(Integer leftPageNumber, Integer rightPageNumber, Integer currentPageNumber, Integer lastPageNumber, Integer records) {
        this.leftPageNumber = leftPageNumber;
        this.rightPageNumber = rightPageNumber;
        this.currentPageNumber = currentPageNumber;
        this.lastPageNumber = lastPageNumber;
        this.records = records;
    }
}
