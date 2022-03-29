package com.example.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.example.dto.ItemDTO;
import com.example.service.ItemService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/item")
public class ItemController {

    @Autowired
    ItemService iService;

    // 1개 조회
    @GetMapping(value = "/content")
    public String contentGET(
            Model model,
            @RequestParam(name = "code") long code) {
        System.out.println(code);
        long item = iService.selectItemOne(code);
        model.addAttribute("item", item);

        return "item/content";
    }

    // 물품 등록
    @GetMapping(value = "/insert")
    public String inserGET() {
        return "/item/insert";
    }

    @PostMapping(value = "insert")
    public String inserPOST(
            HttpSession httpSession,
            @ModelAttribute ItemDTO item,
            @RequestParam(name = "timage") MultipartFile file)
            throws IOException {
        System.out.println(item.toString());
        System.out.println(file.getOriginalFilename());

        // 파일 관련 내용
        item.setIimagetype(file.getContentType());
        item.setIimagename(file.getOriginalFilename());
        item.setIimagesize(file.getSize());
        item.setIimage(file.getBytes());

        // 세션에서 이메일 꺼내기
        String email = (String) httpSession.getAttribute("SESSION_EMAIL");
        item.setUemail(email);

        int ret = iService.insertItemOne(item);
        if (ret == 1) {
            return "redirect:/item/selectlist";
        }

        return "redirect:/item/selectlist";
    }

    // 물품 목록
    // 127.0.0.1:9090/ROOT/item/selectlist?txt=검색어&page=1
    @GetMapping(value = "/selectlist")
    public String selectListGET(
            @RequestParam(name = "txt", defaultValue = "") String txt,
            @RequestParam(name = "page", defaultValue = "1") int page,
            Model model) {

        Map<String, Object> map = new HashMap<>();
        map.put("txt", txt);
        map.put("start", page * 10 - 9);
        map.put("end", page * 10);
        // page가 1일 때, start 1 end 10
        // page가 2일 때, start 11 end 20
        // page가 3일 때, start 21 end 30

        List<ItemDTO> list = iService.selectItemList(map);
        model.addAttribute("list", list);

        long cnt = iService.selectItemCount(txt);
        // 9 => 1
        // 11 => 2
        // 24 => 3
        model.addAttribute("pages", (cnt - 1) / 10 + 1);

        return "/item/selectlist";
    }

}
