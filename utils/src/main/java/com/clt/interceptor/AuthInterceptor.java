package com.clt.interceptor;

import com.clt.config.JwtConfig;
import com.clt.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtConfig jwtConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求头：token
        String token = request.getHeader(jwtConfig.getHeader());

        //判断token是否存在
        if (token == null || token.isEmpty()) {
            response.setStatus(401);
            response.getWriter().write("{\"code\": 401, \"message\": \"未登录\"}");
            return false;
        }

        //判断token是否过期
        if (!jwtUtil.validateToken(token)) {
            response.setStatus(401);
            response.getWriter().write("{\"code\": 401, \"message\": \"Token 已过期\"}");
            return false;
        }

        //放行
        return true;
    }
}