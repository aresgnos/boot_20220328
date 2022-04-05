package com.example.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

// 로그인이 성공했을 때 자동으로 호출되는 handler
public class MyLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        System.out.println("LoginSuccessHandler");

        User user = (User) authentication.getPrincipal();
        System.out.println(user.toString());

        String role = (String) authentication.getAuthorities().toArray()[0].toString();
        System.out.println(role);

        // /ROOT
        System.out.println(request.getContextPath());

        if (role.equals("ADMIN")) {
            response.sendRedirect(request.getContextPath()
                    + "/security_admin/home");
        } else if (role.equals("SELLER")) {
            response.sendRedirect(request.getContextPath()
                    + "/security_seller/home");
        } else if (role.equals("CUSTOMER")) {
            response.sendRedirect(request.getContextPath()
                    + "/security_customer/home");
        }
    }

}
