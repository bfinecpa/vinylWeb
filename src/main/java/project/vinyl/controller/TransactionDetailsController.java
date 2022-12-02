package project.vinyl.controller;

import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.vinyl.constant.SessionConst;
import project.vinyl.dto.TransactionDetailsDto;
import project.vinyl.entity.Member;
import project.vinyl.entity.TransactionDetails;
import project.vinyl.service.TransactionDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransactionDetailsController {

    private final TransactionDetailsService transactionDetailsService;



    @GetMapping("/completeTransaction")
    public String completeTransaction(@RequestParam Long messageRoomId, Model model){
        model.addAttribute("messageRoomId", messageRoomId);
        return "transactionDetails/completeTransaction";
    }

    @PostMapping("/completeTransaction")
    public String completeTransaction(@RequestParam Long messageRoomId, @RequestParam Double tradingRate, Model model, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        transactionDetailsService.saveTransactionCompleted(member.getId(), messageRoomId, tradingRate);
        return "redirect:/";
    }

    @GetMapping("/transactionCompletedList")
    public String transactionCompletedList(HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        List<TransactionDetailsDto> transDetailDto = transactionDetailsService.getTransDetailDto(member.getId());
        model.addAttribute("transDetailDto", transDetailDto);
        return "transactionDetails/transactionCompletedList";
    }
}
