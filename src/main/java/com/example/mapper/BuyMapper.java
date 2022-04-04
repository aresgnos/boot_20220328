package com.example.mapper;

import java.util.List;
import java.util.Map;

import com.example.dto.BuyDTO;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BuyMapper {

        // INSERT INTO 테이블명(컬럼명들...) VALUES(값들...)
        @Insert({
                        "INSERT INTO BUY(BNO, BCNT, BREGDATE, ICODE, UEMAIL)",
                        "VALUES (SEQ_BUY_NO.NEXTVAL, #{obj.bcnt}, CURRENT_DATE, #{obj.icode}, #{obj.uemail})"
        })
        public int insertBuyOne(
                        @Param(value = "obj") BuyDTO buy);

        // 주문내역 조회 리턴은 DTO가 아닌 MAP 컬렉션을 이용함
        @Select({
                        "SELECT * FROM BUYLIST_VIEW WHERE UEMAIL=#{email}"
        })
        public List<Map<String, Object>> selectBuyListMap(@Param(value = "email") String email);

}