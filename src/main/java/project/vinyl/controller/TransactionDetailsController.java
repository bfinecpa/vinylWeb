package project.vinyl.controller;

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
import project.vinyl.service.TransactionDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    public String completeTransaction(@RequestParam Long messageRoomId, Model model, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        transactionDetailsService.saveTransactionCompleted(member.getId(), messageRoomId);
        return "redirect:/";
    }

    @GetMapping("/transactionCompletedList")
    public String transactionCompletedList(HttpServletRequest request, Model model, Pageable pageable){
        HttpSession session = request.getSession(false);
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        Page<TransactionDetailsDto> transDetailDto = transactionDetailsService.getTransDetailDto(member.getId(), pageable);
        model.addAttribute("transDetailDto", transDetailDto);
        return "transactionDetails/transactionCompletedList";
    }
}
