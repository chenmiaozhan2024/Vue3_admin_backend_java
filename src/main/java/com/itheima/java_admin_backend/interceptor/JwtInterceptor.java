package com.itheima.java_admin_backend.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.java_admin_backend.common.JwtUtils;
import com.itheima.java_admin_backend.common.Result;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/**
 * 登录校验拦截器：对应 Node 版的 verifyToken 中间件
 * 从请求头 token 中取 JWT，校验通过后把 userId 放入 request 域供后续使用
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    public static final String USER_ID = "userId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 跨域预检请求直接放行
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }
        String token = request.getHeader("token");
        if (token == null || token.isBlank()) {
            writeError(response, "未提供认证令牌");
            return false;
        }
        try {
            Claims claims = jwtUtils.parseToken(token);
            request.setAttribute(USER_ID, Long.valueOf(claims.getSubject()));
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 签名不合法、格式错误、已过期等
            writeError(response, "认证令牌无效或已过期");
            return false;
        }
    }

    /*
    * 返回 401 + 统一响应体，前端 axios 拦截器会提示“Token 过期”
    * */
    private void writeError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(
                new Result(401, message, null, false)));
    }
}
