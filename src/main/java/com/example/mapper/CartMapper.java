package com.example.mapper;

import com.example.dto.CartDTO;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CartMapper {

    // INSERT INTO 테이블명(컬럼명들...) VALUES(값들...)
    @Insert({
            "INSERT INTO CART(CNO, CCNT, ICODE, UEMAIL)",
            "VALUES (SEQ_CART_NO.NEXTVAL, #{obj.ccnt}, #{obj.icode}, #{obj.uemail})"
    })
    public int insertCartOne(
            @Param(value = "obj") CartDTO cart);

}
