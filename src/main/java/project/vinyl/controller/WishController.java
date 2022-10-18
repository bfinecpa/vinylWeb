package project.vinyl.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.vinyl.constant.SessionConst;
import project.vinyl.dto.CRUDWishItemDto;
import project.vinyl.entity.Member;
import project.vinyl.service.WishService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WishController {

    private final WishService wishService;

    @PostMapping("/setWishItem")
    public String setWishItem(@RequestParam("itemId") Long itemId, RedirectAttributes redirectAttributes,
                              HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        wishService.setWishItem(member.getId(), itemId);
        redirectAttributes.addAttribute("itemId", itemId);
        return  "redirect:/item/{itemId}";
    }

    @GetMapping("/getWishList")
    public String getWishList(Model model, Pageable pageable, HttpServletRequest request){

        HttpSession session = request.getSession(false);
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        Page<CRUDWishItemDto> wishItem = wishService.getWishItem(member.getId(), pageable);
        model.addAttribute("wishItem", wishItem);
        return "wish/wishList";
    }


}
