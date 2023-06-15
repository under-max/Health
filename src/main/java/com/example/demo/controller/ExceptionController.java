package com.example.demo.controller;

import com.example.demo.exception.JJinBBangException;
import com.example.demo.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.reflect.Field;


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

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> illegalArgumentExHandler(IllegalArgumentException e) {

        log.info("exception handler 호출");
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("BAD")
                .message(e.getMessage())
                .build();

        log.info("errorResponse={}", errorResponse);

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
