package com.example.dto;

import java.util.Date;

import lombok.Data;

@Data
public class BuyDTO {
    // 주문번호
    private Long bno;
    // 주문수량
    private Long bcnt;
    // 주문일자
    private Date bregdate;
    // 물품코드
    private Long icode;
    // 이메일
    private String uemail;
}