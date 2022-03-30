package com.example.service;

import java.util.List;

import com.example.dto.ItemImageDTO;

import org.springframework.stereotype.Service;

@Service
public interface ItemImageService {

    // 서브 이미지 일괄 등록
    public int insertItemImageBatch(List<ItemImageDTO> list);

    // 서브 이미지 1개 가져오기
    public ItemImageDTO selectItemImageOne(long imgcode);

    // 물품코드가 일치하는 모든 이미지코드 가져오기
    public List<Long> selectItemImageList(long icode);

    // 서브 이미지 1개 수정
    public int updateItemImageOne(ItemImageDTO itemimage);

    // 서브 이미지 1개 삭제
    public int deleteItemImageOne(long imgcode);

}
