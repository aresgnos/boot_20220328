package com.example.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class HttpInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) throws Exception {
        // 주소정보
        String pathName = request.getServletPath();

        // ?뒤의 쿼리정보
        String queryName = request.getQueryString();

        // 세션객체 가져오기
        HttpSession httpSession = request.getSession();

        httpSession.setAttribute("BACKURL", pathName);
        if (queryName != null) {
            httpSession.setAttribute("BACKURL", pathName + "?" + queryName);
        }

        System.out.println("-----------------start interceptor---------------");
        System.out.println(pathName + ", " + queryName);
        System.out.println("BACKURL : " + httpSession.getAttribute("BACKURL"));
        System.out.println("-------------------end interceptor---------------");
    }

}