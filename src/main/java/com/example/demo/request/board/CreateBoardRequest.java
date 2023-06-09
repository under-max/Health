package com.example.demo.request.board;

import lombok.Data;

@Data
public class CreateBoardRequest {
    private String title;
    private String content;
}
