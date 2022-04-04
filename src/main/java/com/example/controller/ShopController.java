package com.example.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.dto.BuyDTO;
import com.example.dto.CartDTO;
import com.example.dto.ItemDTO;
import com.example.mapper.BuyMapper;
import com.example.mapper.CartMapper;
import com.example.mapper.ItemImageMapper;
import com.example.mapper.ItemMapper;

@Controller
@RequestMapping(value = "/shop")
public class ShopController {

    @Autowired
    ItemMapper iMapper;
    @Autowired
    ItemImageMapper iiMapper;
    @Autowired
    CartMapper cMapper;
    @Autowired
    BuyMapper bMapper;

    @Autowired
    HttpSession httpSession;

    @GetMapping(value = "/buylist")
    public String buylistGET(Model model) {
        // 세션에서 로그인된 사용자의 정보를 가져옴
        String email = (String) httpSession.getAttribute("M_EMAIL");
        if (email == null) { // 로그인 되지 않았따면
            return "redirect:/member/login";
        }
        // 로그인 되었을 때
        List<Map<String, Object>> list = bMapper.selectBuyListMap(email);
        model.addAttribute("list", list);
        return "/shop/buylist";
    }

    // http://127.0.0.1:9090/ROOT/shop/cart?cnt=1&code=1
    @PostMapping(value = "/cart")
    public String cartPOST(
            @RequestParam(name = "btn") String btn,
            @RequestParam(name = "cnt") long cnt,
            @RequestParam(name = "code") long code) {
        String email = (String) httpSession.getAttribute("M_EMAIL");
        System.out.println(email);

        if (email == null) { // 로그인 되지 않았다면
            return "redirect:/member/login";
        }
        System.out.println(btn);

        if (btn.equals("장바구니")) {
            CartDTO cart = new CartDTO();
            cart.setCcnt(cnt); // 수량
            cart.setIcode(code); // 물품코드
            cart.setUemail(email);
            cMapper.insertCartOne(cart);
            return "redirect:/shop/cartlist";

        } else if (btn.equals("주문하기")) {
            BuyDTO buy = new BuyDTO();
            buy.setBcnt(cnt);
            buy.setIcode(code);
            buy.setUemail(email);

            bMapper.insertBuyOne(buy);
            return "redirect:/shop/buylist";
        }
        return "redirect:/";
    }

    // http://127.0.0.1:9090/ROOT/shop/detail?code=14
    @GetMapping(value = { "/detail" })
    public String shopGET(Model model,
            @RequestParam(name = "code") long code) {
        // 물품 상세 정보
        ItemDTO item = iMapper.selectItemOne(code);
        System.out.println(item.toString());
        model.addAttribute("obj", item);

        // 물품에 대한 서브이미지 번호들
        List<Long> list = iiMapper.selectItemImageCodeList(code);
        model.addAttribute("list", list);

        return "/shop/detail";
    }

    // http://127.0.0.1:9090/ROOT/shop/home
    // http://127.0.0.1:9090/ROOT/shop
    @GetMapping(value = { "/", "/home" })
    public String shopGET(Model model) {
        // 등록일
        List<ItemDTO> list1 = iMapper.selectItemList(1);
        model.addAttribute("list1", list1);

        // 물품명
        List<ItemDTO> list2 = iMapper.selectItemList(2);
        model.addAttribute("list2", list2);

        // 가격
        List<ItemDTO> list3 = iMapper.selectItemList(3);
        model.addAttribute("list3", list3);

        return "/shop/home";
    }

}
