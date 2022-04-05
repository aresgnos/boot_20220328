package com.example.controller;

import com.example.dto.MemberDTO;
import com.example.dto.MyUserDTO;
import com.example.mapper.MemberMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SecurityController {
    // 환경설정

    @Autowired
    MemberMapper mMapper;

    @GetMapping(value = { "/security_403" })
    public String security403GET() {
        return "/security/403page";
    }

    // 관리자 화면은 관리자만 볼 수 있게
    // 페이지의 권한을 조절
    // 관리자(admin), 판매자(seller), 고객(customer) 각각 로그인했을 때 접근할 수 있는 페이지를 제한

    // 로그인 안해도 보이는 페이지
    // 127.0.0.1:9090/security_home
    @GetMapping(value = { "/security_home" })
    public String securityHomeGET(
            Model model,
            @AuthenticationPrincipal MyUserDTO user) { // serviceimpl에서 세션에 넣음 -> 세션에서 받아옴
        if (user != null) {
            System.out.println(user.getUsername());
            System.out.println(user.getName());
            System.out.println(user.getUserphone());
            System.out.println(user.getAuthorities().toArray()[0]);
        }
        model.addAttribute("user", user);
        // model.addAttribute("userid", user.getUsername());
        // model.addAttribute("userrole", user.getAuthorities().toArray());
        return "/security/home";
    }

    // 로그인 후에 보이는 페이지
    // 127.0.0.1:9090/security_admin/home
    @GetMapping(value = { "/security_admin/home" })
    public String securityAdminHomeGET() {
        return "/security/admin_home";
    }

    // 로그인 후에 보이는 페이지
    // 127.0.0.1:9090/ROOT/security_seller/home
    @GetMapping(value = { "/security_seller/home" })
    public String securitySellerHomeGET() {
        return "/security/seller_home";
    }

    // 로그인 후에 보이는 페이지
    // 127.0.0.1:9090/ROOT/security_customer/home
    @GetMapping(value = { "/security_customer/home" })
    public String securityCustomerHomeGET() {
        return "/security/customer_home";
    }

    // 127.0.0.1:9090/member/security_join
    @GetMapping(value = "/member/security_join")
    public String securityJoinGET() {
        return "/security/join";
    }

    @PostMapping(value = "/member/security_join")
    public String securityJoinPOST(
            @ModelAttribute MemberDTO member) {
        BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
        // 암호를 가져와서 해시한 후 다시 추가하기
        member.setUpw(bcpe.encode(member.getUpw()));
        member.setUrole("CUSTOMER");

        int ret = mMapper.memberJoin(member);

        if (ret == 1) { // 성공 시
            return "redirect:/security_home";
        }
        // 실패 시
        return "redirect:/member/security_join";
    }

    @GetMapping(value = "member/security_login")
    public String securityLoginGET() {
        return "/security/login";
    }

}
