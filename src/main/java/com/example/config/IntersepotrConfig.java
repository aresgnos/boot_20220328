package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 인터셉터 환경설정
// 주소로만 필터 처리
@Configuration
public class IntersepotrConfig implements WebMvcConfigurer {

    @Autowired
    HandlerInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor)
                .addPathPatterns("/**")
                .excludePathPatterns( // 수행되지 않아야 하는 주소를 설정
                        "/member/login**", "/member/logout**", "/item/image/**", "/item/subimage/**",
                        "/shop/cart/**", "/resources/**");
    }
}
