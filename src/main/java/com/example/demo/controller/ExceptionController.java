package com.example.demo.controller;

import com.example.demo.exception.JJinBBangException;
import com.example.demo.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


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



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        int statusCode = e.getStatusCode().value();
        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message(e.getFieldErrors().get(0).getDefaultMessage())
                .build();
        return  ResponseEntity.status(statusCode)
                .body(response);
    }
}
