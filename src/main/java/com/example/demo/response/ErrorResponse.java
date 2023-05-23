package com.example.demo.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


//@JsonInclude(value = JsonInclude.Include.NON_EMPTY)//값이없으면 전송하지 않음
@Setter
public class ErrorResponse {
    private final String code;
    private final String message;

    private final Map<String, String> validation;

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation =validation;
    }

    public void addValidation(String fieldName, String errorMessage){
        this.validation.put(fieldName, errorMessage);
    }
}
