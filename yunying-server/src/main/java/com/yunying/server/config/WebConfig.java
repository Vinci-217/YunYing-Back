package com.yunying.server.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 允许 http://localhost:3000 发起跨域请求
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // 允许的域名
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的方法
                .allowedHeaders("*") // 允许的请求头
                .allowCredentials(true); // 允许携带凭证（如 cookies）
    }
}
