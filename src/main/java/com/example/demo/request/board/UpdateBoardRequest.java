package com.example.demo.request.board;

import lombok.Data;

@Data
public class UpdateBoardRequest {
    private String title;
    private String content;
}
