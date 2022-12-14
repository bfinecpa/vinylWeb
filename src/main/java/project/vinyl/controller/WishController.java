package project.vinyl.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.vinyl.constant.SessionConst;
import project.vinyl.dto.CRUDWishItemDto;
import project.vinyl.entity.Member;
import project.vinyl.service.WishService;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.module.FindException;
import java.util.HashMap;
import java.util.Map;
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
        try{
            wishService.setWishItem(member.getId(), itemId);
            redirectAttributes.addAttribute("itemId", itemId);
            return  "redirect:/item/{itemId}";
        }catch (EntityExistsException e){
            String errors = "이미 찜한 상품 입니다.";
            redirectAttributes.addAttribute("errors", errors);
            redirectAttributes.addAttribute("itemId", itemId);
            return  "redirect:/item/{itemId}";
        }catch (Exception e){
            String errors = "현 상품 판매자는 찜할 수 없습니다.";
            redirectAttributes.addAttribute("errors", errors);
            redirectAttributes.addAttribute("itemId", itemId);
            return  "redirect:/item/{itemId}";
        }
    }

    @GetMapping(value = {"/getWishList/{page}", "/getWistList"})
    public String getWishList(@PathVariable("page") Optional<Integer> page, Model model, HttpServletRequest request ){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);

        HttpSession session = request.getSession(false);
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);

        Page<CRUDWishItemDto> wishItem = wishService.getWishItem(member.getId(), pageable);

        model.addAttribute("wishItem", wishItem);
        model.addAttribute("maxPage", 5);
        return "wish/wishList";
    }


    @PostMapping("/wishItem/delete")
    public String delete(@RequestParam("itemId") Long wishItemId, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        wishService.deleteWishItem(member.getId(), wishItemId);
        return "redirect:/getWistList";
    }

}
