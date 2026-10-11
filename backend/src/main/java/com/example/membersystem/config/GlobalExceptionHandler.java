package com.example.membersystem.config;

import com.example.membersystem.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 處理欄位驗證錯誤 (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return ApiResponse.error(errorMessage);
    }

    // 處理商業邏輯非法參數 (如帳號重複、密碼錯誤等)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ApiResponse.error(ex.getMessage());
    }

    // 處理權限不足與非法狀態 (如試圖刪除或修改他人帳號)
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<Void> handleIllegalStateException(IllegalStateException ex) {
        return ApiResponse.error(ex.getMessage());
    }

    // 處理其他未預期異常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleGeneralException(Exception ex) {
        return ApiResponse.error("系統發生錯誤，請稍後再試: " + ex.getMessage());
    }
}
