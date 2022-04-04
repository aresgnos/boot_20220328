package com.example.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CartDTO {
    // 카트번호
    private Long cno;
    // 수량
    private Long ccnt;
    // 등록일
    private Date cregdate;
    // 물품코드
    private Long icode;
    // 이메일
    private String uemail;
}