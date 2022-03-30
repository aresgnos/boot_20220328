package com.example.service;

import java.util.List;
import java.util.Map;

import com.example.dto.ItemDTO;

import org.springframework.stereotype.Service;

@Service
public interface ItemService {

    // 물품 등록
    public int insertItemOne(ItemDTO item);

    // 물품 목록(검색어+페이지네이션)
    public List<ItemDTO> selectItemList(Map<String, Object> map);

    // 물품 전체 개수 구하기
    public long selectItemCount(Map<String, Object> map);

    // 물품 1개 조회
    public ItemDTO selectItemOne(long code);

    // 이미지 1개 조회
    public ItemDTO selectItemImageOne(long code);

}
