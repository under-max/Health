package com.example.demo.controller;

import com.example.demo.exception.JJinBBangException;
import com.example.demo.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@Slf4j
@ControllerAdvice
public class ExceptionController {
    @ResponseBody
    @ExceptionHandler(JJinBBangException.class)
    public ResponseEntity<ErrorResponse> JJinBBangExceptionHandler(JJinBBangException e) {
        int statusCode = e.getStatusCode();
        ErrorResponse response = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();
        return ResponseEntity.status(statusCode)
                .body(response);
    }
}
