package com.clt.config;

import com.clt.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册认证拦截器（验证 Token）
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**") // 拦截所有 API 请求
                .excludePathPatterns("/auth/**",
                        "/problems/**",
                        "/users/*",
                        "/echarts/**"); // 排除登录注册接口
    }
}
