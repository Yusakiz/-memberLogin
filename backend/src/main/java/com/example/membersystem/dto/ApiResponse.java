package com.example.membersystem.dto;

/**
 * 統一 API 回應封裝物件 (Unified API Response)
 * 讓所有 API 回傳給前端的 JSON 格式都長得一致：
 * {
 *   "success": true/false,
 *   "message": "說明文字",
 *   "data": 資料內容
 * }
 */
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public ApiResponse() {}

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, "Success", data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
