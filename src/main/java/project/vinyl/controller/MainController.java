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
import project.vinyl.constant.SessionConst;
import project.vinyl.dto.ItemSearchDto;
import project.vinyl.dto.MainPageItemDto;
import project.vinyl.entity.Member;
import project.vinyl.service.ItemService;

import javax.print.attribute.standard.PageRanges;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final ItemService itemService;

    @GetMapping(value={"/", "/{page}"})
    public String mainPage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 20);
        Page<MainPageItemDto> items = itemService.getMainPageItems(itemSearchDto, pageable);
        if(items.getContent().isEmpty()){
            log.info("비어있음");
        }
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        return "main";
    }

    @GetMapping("/myPage")
    public String myPage(HttpServletRequest request, Model model) {

        // 이 부분 없어도 어차피 로그인 화면으로 넘어감,,,
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "login/loginForm";
        }
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (member == null) {
            return "login/loginForm";
        }

        Double tradingRate = member.getTradingRate();
        Long countRatingPerson = member.getCountRatingPerson();
        Double rate = tradingRate/countRatingPerson;
        String memberRate = String.format("%.1f", rate);

        model.addAttribute("member", member);
        model.addAttribute("rate", memberRate);
        return "myPage";



    }
}
