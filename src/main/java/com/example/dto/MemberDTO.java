package com.example.dto;

import java.util.Date;
import lombok.Data;

@Data
public class MemberDTO {
    // 이메일
    private String uemail;
    // 암호
    private String upw;
    // 이름
    private String uname;
    // 연락처
    private String uphone;
    // 권한
    private String urole;
    // 등록일
    private Date uregdate;

    // 날짜 포맷을 바꿔서 보관하기 위한 변수
    private String uregdate1;

}
