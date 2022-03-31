package com.example.dto;

import java.util.Date;

import lombok.Data;

@Data
public class MemberaddrDTO {

    // 주소코드
    private Long ucode;
    // 전체주소
    private String uaddresss;
    // 우편번호
    private String upostcode;
    // 등록일
    private Date uregdate;
    // 이메일
    private String uemail;
    // 대표주소
    private Long uchk;

}
