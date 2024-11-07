package com.yunying.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 配置全局跨域
        registry.addMapping("/**")  // 允许所有路径进行跨域
                .allowedOrigins("http://localhost:3000")  // 允许的跨域来源
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 允许的 HTTP 方法
                .allowedHeaders("*")  // 允许的请求头
                .allowCredentials(true)  // 允许带上凭证（如 cookies）
                .maxAge(3600);  // 预检请求的缓存时间
    }
}
