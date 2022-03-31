package com.example.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.example.dto.MemberDTO;
import com.example.dto.MemberaddrDTO;
import com.example.mapper.MemberMapper;
import com.example.mapper.MemberaddrMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/member")
public class MemberController {

    @Autowired
    MemberMapper mMapper;
    @Autowired
    HttpSession httpSession;
    @Autowired
    MemberaddrMapper maMapper;

    @GetMapping(value = "/updateaddr")
    public String updateaddrGET(
            Model model,
            @RequestParam(name = "code") long code) {
        String email = (String) httpSession.getAttribute("M_EMAIL");
        String role = (String) httpSession.getAttribute("M_ROLE");

        if (email != null) { // 로그인 되었는지
            if (role.equals("CUSTOMER")) { // 권한이 정확한지
                MemberaddrDTO addr = maMapper.updateMemberAddress(code);
                model.addAttribute("obj", addr);
                return "redirect:/member/address";
            }
        }
        return "redirect:/member/address";
    }

    // 주소 삭제
    @GetMapping(value = "/deleteaddr")
    public String deleteaddrGET(
            @RequestParam(name = "code") long code) {
        String email = (String) httpSession.getAttribute("M_EMAIL");
        String role = (String) httpSession.getAttribute("M_ROLE");

        if (email != null) { // 로그인 되었는지
            if (role.equals("CUSTOMER")) { // 권한이 정확한지
                int ret = maMapper.deleteMemberAddress(code);
                if (ret == 1) {
                    return "redirect:/member/address";
                }
            }
        }
        return "redirect:/member/address";
    }

    // 대표주소설정
    @PostMapping(value = "/setaddr")
    public String setaddrPOST(
            @RequestParam(name = "code") long code) {
        System.out.println(code);

        String email = (String) httpSession.getAttribute("M_EMAIL");
        String role = (String) httpSession.getAttribute("M_ROLE");

        if (email != null) { // 로그인 되었는지
            if (role.equals("CUSTOMER")) { // 권한이 정확한지
                int ret = maMapper.updateMemberAddressSet(email, code);
                if (ret == 1) {
                    return "redirect:/member/address";
                }
            }
        }
        return "redirect:/member/login";
    }

    // 주소관리
    @GetMapping(value = "/address")
    public String addressGET(Model model) {
        // getAttribute(로그인할 때 세션 키)
        String email = (String) httpSession.getAttribute("M_EMAIL");
        String role = (String) httpSession.getAttribute("M_ROLE");

        if (email != null) { // 로그인 되었는지
            if (role.equals("CUSTOMER")) { // 권한이 정확한지
                // 화면 표시하기

                // view 전송해야 됨
                List<MemberaddrDTO> list = maMapper.selectMemberAddressList(email);
                model.addAttribute("addr", list);
                return "/member/address";
            }
            return "redirect:/403page";

        } // 로그인 안 했을 경우
        return "redirect:/member/login";
    }

    // 주소등록
    // 로그인한 상태에서 가능
    // 따라서 email은 session에서 꺼냄
    @PostMapping(value = "/addressaction")
    public String addressPOST(@ModelAttribute MemberaddrDTO addr) {
        String email = (String) httpSession.getAttribute("M_EMAIL");
        String role = (String) httpSession.getAttribute("M_ROLE");

        if (email != null) { // 로그인 되었는지
            if (role.equals("CUSTOMER")) { // 권한이 정확한지
                addr.setUemail(email);
                System.out.println(addr.toString());
                int ret = maMapper.insertMemberAddress(addr);
                if (ret == 1) {
                    return "redirect:/member/address";
                }

            }
        }
        return "redirect:/member/login";
    }

    @GetMapping(value = "/addrget")
    public String addrgetGET() {

        return null;
    }

    // 회원가입
    @GetMapping(value = "/join")
    public String joinGET() {
        return "/member/join";
    }

    @PostMapping(value = "/joinaction")
    public String joinactionPOST(
            @ModelAttribute MemberDTO member) {

        // 필요한 항목을 수동으로 채울 수 있다.
        member.setUrole("CUSTOMER");

        // 3. 객체의 값이 잘 들어갔는지 확인
        System.out.println(member.toString());

        int ret = mMapper.memberJoin(member);
        if (ret == 1) {
            // redirect는 주소를 변경한 후에 엔터키를 누른 것처럼 화면을 띄움
            // 회원가입 된 것처럼 하고 홈으로(주소를 바꾸는 것)
            return "redirect:/";
        }
        return "redirect:/member/join";

        // templates의 member폴더의 join을 표시 (렌더링)
        // render는 GET에서만 사용해야함.
        // return "/member/join";
    }

    // 로그인
    @GetMapping(value = "/login")
    public String loginGET() {
        return "member/login";
    }

    @PostMapping(value = "/loginaction")
    public String loginactionPOST(
            @RequestParam(name = "uemail") String email,
            @RequestParam(name = "upw") String pw) {
        System.out.println(email);
        System.out.println(pw);

        MemberDTO member = mMapper.memberLogin(email, pw);
        if (member != null) {
            // 반환되는 로그인 정보를 출력
            System.out.println(member.toString());
            httpSession.setAttribute("M_EMAIL", member.getUemail());
            httpSession.setAttribute("M_NAME", member.getUname());
            httpSession.setAttribute("M_ROLE", member.getUrole());
            return "redirect:/";
        }
        return "redirect:/member/login";
    }

    // 로그아웃
    // logout은 보통 post
    @PostMapping(value = "/logout")
    public String logoutPOST() {
        // invalidate => 세션의 무효화
        httpSession.invalidate();
        return "redirect:/";
    }

}
