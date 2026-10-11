package com.example.membersystem.security;

import com.example.membersystem.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 身分驗證攔截器 (AuthInterceptor)
 * 站在門口檢查請求是否帶有合法的 JWT Token (滿足條件 6)
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public AuthInterceptor(JwtUtil jwtUtil, ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 放行瀏覽器的 CORS 預檢請求 (OPTIONS)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 2. 從 HTTP 標頭中拿出 Authorization: Bearer <Token>
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // 3. 驗證 Token 是否合法 (未過期且簽名正確)
            if (jwtUtil.validateToken(token)) {
                Long memberId = jwtUtil.getMemberIdFromToken(token);

                // 4. 將當前登入者 ID 掛在 Request 屬性上，供後續 Controller 使用
                request.setAttribute("currentMemberId", memberId);
                return true; // 放行進入 Controller！
            }
        }

        // 5. 沒帶 Token 或 Token 無效，直接回傳 HTTP 401 拒絕進門！
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        ApiResponse<Void> apiResponse = ApiResponse.error("請先登入後再進行此操作");
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        return false;
    }
}
