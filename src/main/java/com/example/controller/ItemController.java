package com.example.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.example.dto.ItemDTO;
import com.example.dto.ItemImageDTO;
import com.example.service.ItemImageService;
import com.example.service.ItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    ResourceLoader resLoader;

    @Autowired
    ItemImageService iiService;

    // 서브 이미지 가져오기
    // @GetMapping(value = "/subimage")
    // public ResponseEntity<byte[]> subimageGET(
    // @RequestParam(name = "imgcode") long imgcode) throws IOException {

    // // 이미지명, 이미지크기, 이미지종류, 이미지데이터
    // ItemImageDTO img = iiService.selectItemImageOne(imgcode);
    // System.out.println(img.getIimagetype());

    // if (img.getIimagesize() > 0) { // 첨부한 파일이 있으면
    // HttpHeaders headers = new HttpHeaders();

    // // 이미지 타입에 따라 headers에 종류를 넣어줌.
    // if (img.getIimagetype().equals("image/jpeg")) {
    // headers.setContentType(MediaType.IMAGE_JPEG);
    // } else if (img.getIimagetype().equals("image/png")) {
    // headers.setContentType(MediaType.IMAGE_PNG);
    // } else if (img.getIimagetype().equals("image/gif")) {
    // headers.setContentType(MediaType.IMAGE_GIF);
    // }

    // // 이미지 byte[], headers, HttpStatus.Ok
    // ResponseEntity<byte[]> response = new ResponseEntity<>(img.getIimage(),
    // headers, HttpStatus.OK);
    // return response;
    // }

    // return null;
    // }

    // 서브이미지 첨부
    @PostMapping(value = "insertimages")
    public String insertimagesPOST(
            @RequestParam(name = "icode") long icode,
            @RequestParam(name = "timage") MultipartFile[] files) throws IOException {

        // ItemImageDTO를 n개 보관할 수 있는 list
        List<ItemImageDTO> list = new ArrayList<>();

        for (MultipartFile file : files) {
            ItemImageDTO obj = new ItemImageDTO();
            obj.setIcode(icode); // 물품코드
            obj.setIimage(file.getBytes());
            obj.setIimagetype(file.getContentType());
            obj.setIimagesize(file.getSize());
            obj.setIimagename(file.getOriginalFilename());

            list.add(obj);
        }

        int ret = iiService.insertItemImageBatch(list);
        if (ret == 1) {
            return "redirect:/item/content?code=" + icode;
        }
        return "redirect:/item/content?code=" + icode;
    }

    // 이미지 1개 조회
    // 127.0.0.1:9090/ROOT/item/image?code=15
    // html에서 사용 시 th:src=<img th:src="@{/item/image(code=15)}">
    @GetMapping(value = "/image")
    public ResponseEntity<byte[]> imageGET(
            @RequestParam(name = "code") long code) throws IOException {

        // 이미지명, 이미지크기, 이미지종류, 이미지데이터
        ItemDTO item = iService.selectItemImageOne(code);
        System.out.println(item.getIimagetype());
        if (item != null) { // 물품 정보가 있으면
            if (item.getIimagesize() > 0) { // 첨부한 파일이 있으면
                HttpHeaders headers = new HttpHeaders();

                // 이미지 타입에 따라 headers에 종류를 넣어줌.
                if (item.getIimagetype().equals("image/jpeg")) {
                    headers.setContentType(MediaType.IMAGE_JPEG);
                } else if (item.getIimagetype().equals("image/png")) {
                    headers.setContentType(MediaType.IMAGE_PNG);
                } else if (item.getIimagetype().equals("image/gif")) {
                    headers.setContentType(MediaType.IMAGE_GIF);
                }

                // 이미지 byte[], headers, HttpStatus.Ok
                ResponseEntity<byte[]> response = new ResponseEntity<>(item.getIimage(),
                        headers, HttpStatus.OK);
                return response;
            } else {
                InputStream is = resLoader
                        .getResource("classpath:/static/img/default1.jpg")
                        .getInputStream();

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);

                ResponseEntity<byte[]> response = new ResponseEntity<>(is.readAllBytes(),
                        headers, HttpStatus.OK);

                return response;
            }
        }
        return null;
    }

    // 상세화면
    // 127.0.0.1:9090/ROOT/item/content?code=15
    @GetMapping(value = "/content")
    public String contentGET(
            Model model,
            @RequestParam(name = "code") long code) {
        ItemDTO item = iService.selectItemOne(code);
        model.addAttribute("item", item);

        // 물품코드가 일치하는 모든 이미지코드 가져오기
        List<Long> imgcode = iiService.selectItemImageList(code);
        model.addAttribute("imgcode", imgcode);

        // 서브이미지 가져오기
        ItemImageDTO image = iiService.selectItemImageOne(code);
        model.addAttribute("image", imgcode);
        if (image.getIimagesize() > 0) { // 첨부한 파일이 있으면
            HttpHeaders headers = new HttpHeaders();

            // 이미지 타입에 따라 headers에 종류를 넣어줌.
            if (image.getIimagetype().equals("image/jpeg")) {
                headers.setContentType(MediaType.IMAGE_JPEG);
            } else if (image.getIimagetype().equals("image/png")) {
                headers.setContentType(MediaType.IMAGE_PNG);
            } else if (image.getIimagetype().equals("image/gif")) {
                headers.setContentType(MediaType.IMAGE_GIF);
            }
        }

        return "/item/content";
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
            HttpSession httpSession,
            @RequestParam(name = "txt", defaultValue = "") String txt,
            @RequestParam(name = "page", defaultValue = "1") int page,
            Model model) {

        // 로그인 되었을 때 selectlist가 보이도록
        // 형변환 무슨 말인지 모르겠다.
        String em = (String) httpSession.getAttribute("SESSION_EMAIL");
        if (em != null) {
            String role = (String) httpSession.getAttribute("SESSION_ROLE");
            if (role.equals("SELLER")) {
                Map<String, Object> map = new HashMap<>();
                map.put("txt", txt);
                map.put("start", page * 10 - 9);
                map.put("end", page * 10);
                map.put("email", em);
                // page가 1일 때, start 1 end 10
                // page가 2일 때, start 11 end 20
                // page가 3일 때, start 21 end 30

                List<ItemDTO> list = iService.selectItemList(map);
                model.addAttribute("list", list);

                long cnt = iService.selectItemCount(map);
                // 9 => 1
                // 11 => 2
                // 24 => 3
                model.addAttribute("pages", (cnt - 1) / 10 + 1);

                return "/item/selectlist";
            }
        }
        return "redirect:/seller/select";
    }
}
