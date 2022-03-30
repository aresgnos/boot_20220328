package com.example.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ItemImageDTO {

    // 물품이미지코드 (시퀀스 사용)
    private Long imgcode;
    // 등록일
    private Date iregdate;
    // 이미지
    private byte[] iimage;
    // 이미지크기
    private long iimagesize;
    // 이미지타입
    private String iimagetype;
    // 이미지명
    private String iimagename;
    // 물품코드
    private Long icode;

}
