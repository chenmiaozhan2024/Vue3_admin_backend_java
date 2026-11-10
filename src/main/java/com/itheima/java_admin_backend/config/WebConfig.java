package com.itheima.java_admin_backend.config;

import com.itheima.java_admin_backend.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.MultipartConfigElement;

/**
 * Web 配置：
 * 1. 上传文件大小限制（默认 1MB 太小，logo 图片容易超）
 * 2. 把 /api/static/img/** 映射到本地 upload 目录，让上传后的图片可以直接通过 URL 访问
 * 3. 跨域配置（对应 Node 版 app.use(cors())）
 * 4. 登录校验拦截器（对应 Node 版 verifyToken 中间件）
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

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

    /*
    * 跨域配置：允许任意来源，允许携带自定义请求头 token
    * */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Spring 6 中 allowedOrigins("*") 与 allowCredentials(true) 不能共存，用 allowedOriginPatterns
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("token")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /*
    * 登录校验：除登录接口、静态资源、错误页外，/admin/** 下的接口都必须携带有效 token
    * */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns(
                        "/admin/acl/index/login",
                        "/api/static/**",
                        "/error"
                );
    }
}
