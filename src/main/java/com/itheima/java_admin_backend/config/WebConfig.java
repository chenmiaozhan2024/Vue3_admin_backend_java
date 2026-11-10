package com.itheima.java_admin_backend.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.MultipartConfigElement;

/**
 * Web 配置：
 * 1. 上传文件大小限制（默认 1MB 太小，logo 图片容易超）
 * 2. 把 /api/static/img/** 映射到本地 upload 目录，让上传后的图片可以直接通过 URL 访问
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("10MB");
        factory.setMaxRequestSize("20MB");
        return factory.createMultipartConfig();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 上传目录：{项目运行目录}/upload/img/sph/...
        String uploadPath = System.getProperty("user.dir") + "/upload/img/";
        registry.addResourceHandler("/api/static/img/**")
                .addResourceLocations("file:" + uploadPath);
    }
}
