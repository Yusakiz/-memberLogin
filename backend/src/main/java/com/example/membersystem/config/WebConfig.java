package com.example.membersystem.config;

import com.example.membersystem.security.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 設定 (WebConfig)
 * 負責：
 * 1. 註冊攔截器 (設定哪些網址要查票、哪些網址放行)
 * 2. 設定 CORS (跨來源資源共用，允許 Vue 5173 呼叫後端 8080)
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    // 1. 設定跨域 (CORS)
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    // 2. 註冊攔截器與指定巡邏路線
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                // 指定要查票的網址：修改、刪除、以及獲取個人資料
                .addPathPatterns("/api/members/me", "/api/members/**")
                // 排除不需要查票的公開網址：查詢會員、註冊、登入、以及 H2 資料庫後台
                .excludePathPatterns("/api/members/search", "/api/auth/**", "/h2-console/**");
    }
}
