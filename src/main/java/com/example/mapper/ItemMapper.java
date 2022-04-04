package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.dto.ItemDTO;

@Mapper
public interface ItemMapper {

	// 물품 정보 1개 조회
	@Select({
			"SELECT",
			"I.ICODE, I.INAME, I.ICONTENT, I.IPRICE, I.IQUANTITY",
			"FROM ITEM I",
			"WHERE I.ICODE = #{code}"
	})
	public ItemDTO selectItemOne(
			@Param(value = "code") long code);

	/*
	 * -- 타입1번 // 등록일 기준 내림차순 1
	 * SELECT * FROM (
	 * SELECT I.ICODE, I.INAME, I.IPRICE, ROW_NUMBER() OVER (ORDER BY IREGDATE DESC)
	 * ROWN FROM ITEM I
	 * )
	 * WHERE ROWN BETWEEN 1 AND 12 ORDER BY ROWN ASC;
	 * 
	 * 
	 * 
	 * -- 타입2번 // 물품명 기준 오름차순 2
	 * SELECT * FROM (
	 * SELECT I.ICODE, I.INAME, I.IPRICE, ROW_NUMBER() OVER (ORDER BY INAME ASC)
	 * ROWN FROM ITEM I
	 * )
	 * WHERE ROWN BETWEEN 1 AND 12 ORDER BY ROWN ASC;
	 * 
	 * 
	 * -- 타입3번 // 가격 기준 오름차순 3
	 * SELECT * FROM (
	 * SELECT I.ICODE, I.INAME, I.IPRICE, ROW_NUMBER() OVER (ORDER BY IPRICE ASC)
	 * ROWN FROM ITEM I
	 * )
	 * WHERE ROWN BETWEEN 1 AND 12 ORDER BY ROWN ASC;
	 */

	@Select({
			"<script>",
			"SELECT * FROM (",
			" SELECT ",
			" I.ICODE, I.INAME, I.IPRICE, ",
			" ROW_NUMBER() OVER (",
			"<choose>",
			"<when test='type == 1'>ORDER BY IREGDATE DESC</when>",
			"<when test='type == 2'>ORDER BY INAME ASC</when>",
			"<when test='type == 3'>ORDER BY IPRICE ASC</when>",
			"</choose>",
			") ROWN ",
			" FROM ",
			"ITEM I",
			") WHERE ROWN BETWEEN 1 AND  12 ORDER BY ROWN ASC",
			"</script>"
	})

	public List<ItemDTO> selectItemList(@Param(value = "type") int type);
}
