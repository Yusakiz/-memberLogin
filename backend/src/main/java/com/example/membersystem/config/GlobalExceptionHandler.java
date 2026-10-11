package com.example.membersystem.config;

import com.example.membersystem.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全域異常處理器 (GlobalExceptionHandler)
 * 負責攔截整個後端丟出來的所有 Exception，並統一轉成漂亮的 ApiResponse JSON 格式
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. 攔截欄位校驗錯誤 (@Valid 沒通過時丟出的異常)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // 把所有沒通過的錯誤訊息串接在一起 (例如: "密碼長度需至少 4 個字元; 會員帳號不得為空")
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return ApiResponse.error(errorMessage);
    }

    // 2. 攔截商業邏輯參數錯誤 (例如: "此帳號已被註冊"、"帳號或密碼錯誤")
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ApiResponse.error(ex.getMessage());
    }

    // 3. 攔截權限與非法操作 (例如: "您無權限修改其他會員帳號")
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<Void> handleIllegalStateException(IllegalStateException ex) {
        return ApiResponse.error(ex.getMessage());
    }

    // 4. 攔截其他所有未預期的一般例外 (最後一道安全防護網)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleGeneralException(Exception ex) {
        return ApiResponse.error("系統發生未預期錯誤，請稍後再試: " + ex.getMessage());
    }
}
