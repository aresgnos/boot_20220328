package com.example.service;

import java.util.Collection;

import com.example.dto.MemberDTO;
import com.example.dto.MyUserDTO;
import com.example.mapper.MemberMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemberDetailServiceImpl implements UserDetailsService {

    @Autowired
    MemberMapper mMapper;

    // 화면에서 로그인을 하면 설계부인 이 곳으로 온다.
    // 로그인에서 입력하는 정보 중에서 아이디를 받음
    // MemberMapper를 이용해서 정보를 가져와서 UserDetails
    // 아이디, 암호, 권한이 필요
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("MemberDetailService : " + username);
        MemberDTO member = mMapper.memberEmail(username);

        // 권한 정보를 문자열, 배열로 만듦
        // ** 권한은 배열로 들어갔다 ** => 꺼낼 때 0,1,2로 꺼내야함.
        String[] strRole = { member.getUrole() };

        // String 배열 권한을 Collection<GrantedAuthority> 타입으로 변환함
        Collection<GrantedAuthority> roles = AuthorityUtils.createAuthorityList(strRole);

        // 아이디, 암호, 권한들...
        // 상단 과정들 => user 타입으로 만드는 것이 목표
        // 세션에 들어감
        MyUserDTO user = new MyUserDTO(member.getUemail(),
                member.getUpw(), roles, member.getUphone(), member.getUname());
        return user;
    }
}
