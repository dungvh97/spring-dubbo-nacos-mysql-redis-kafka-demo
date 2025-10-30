package com.demo.user.exception;

import com.demo.common.exception.BusinessException;
import com.demo.user.response.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ApiResponse<String> handleBusinessException(BusinessException ex) {
        return ApiResponse.fail(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<String> handleException(Exception ex) {
        ex.printStackTrace();
        return ApiResponse.fail("Internal server error");
    }
}
