package com.example.mapper;

import java.util.List;

import com.example.dto.MemberaddrDTO;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MemberaddrMapper {

        // 1개의 주소를 수정하기
        // UPDATE 테이블명 SET 변경컬럼명=변경값 WHERE 조건
        @Update({
                        "UPDATE MEMBERADDR SET UADDRESSS=#{uaddresss}, UPOSTCODE=#{upostcode}",
                        " WHERE UCODE=#{obj.ucode} AND UEMAIL=#{obj.uemail}"
        })
        public int updateMemberAddrOne(
                        @Param(value = "obj") MemberaddrDTO addr);

        // 1개의 주소 정보 가져오기
        // SELECT * FROM MEMBERADDR WHERE 조건
        @Select({
                        "SELECT UCODE, UADDRESSS, UPOSTCODE FROM MEMBERADDR ",
                        " WHERE UCODE=#{cd} AND UEMAIL=#{em}"
        })
        public MemberaddrDTO selectMemberAddrOne(
                        @Param(value = "cd") long ucode,
                        @Param(value = "em") String uemail);

        // 주소 삭제
        // DELETE FROM 테이블명 WHERE 조건
        @Delete({
                        "DELETE FROM MEMBERADDR",
                        "WHERE UCODE=#{code} AND UEMAIL=#{em}"
        })
        public int deleteMemberAddrOne(
                        @Param(value = "code") long code,
                        @Param(value = "email") String email);

        // 대표주소 정하기
        // 1. SELECT MAX(UCHK) FROM MEMBERADDR WHERE UEMAIL='ddd' => ddd의 것만 와야되니까
        // = EMAIL, UCHK의 제일 큰 숫자 구하기
        // 2. UPDATE 테이블명 SET 변경할컬럼명 = 1번에서 구한 값+1=변경값 WHERE 조건
        @Update({
                        "UPDATE MEMBERADDR SET UCHK=",
                        "(SELECT MAX(UCHK) FROM MEMBERADDR WHERE UEMAIL=#{email})+1",
                        "WHERE UCODE=#{code}"
        })
        public int updateMemberAddressSet(
                        @Param(value = "email") String email, // 현재 사용자 이메일
                        @Param(value = "code") long code); // 변경하고자하는 코드번호

        // 주소 등록
        @Insert({
                        "INSERT INTO MEMBERADDR(UCODE, UADDRESSS, UPOSTCODE, UEMAIL) ",
                        "VALUES(SEQ_MEMBERADDR_UCODE.NEXTVAL, #{obj.uaddresss},#{obj.upostcode},#{obj.uemail})"
        })
        public int insertMemberAddress(@Param(value = "obj") MemberaddrDTO addr);

        // 주소 목록
        // SELECT * FROM 테이블명 WHERE 조건
        @Select({
                        "SELECT * FROM MEMBERADDR",
                        "WHERE UEMAIL = #{email}"
        })
        public List<MemberaddrDTO> selectMemberAddressList(
                        @Param(value = "email") String email);
}
