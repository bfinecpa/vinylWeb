package project.vinyl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.vinyl.constant.SessionConst;
import project.vinyl.dto.ItemFormDto;
import project.vinyl.dto.LedgerFormDto;
import project.vinyl.dto.TotalLedgerDto;
import project.vinyl.entity.Ledger;
import project.vinyl.entity.Member;
import project.vinyl.service.LedgerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ledger")
public class LedgerController {

    private final LedgerService ledgerService;

    @GetMapping("/new")
    public String addLedger(Model model){
        model.addAttribute("ledgerFormDto", new LedgerFormDto());
        return "ledger/ledgerForm";
    }

    @PostMapping("/new")
    public String addLedger(@Valid LedgerFormDto ledgerFormDto, BindingResult bindingResult,
                            Model model, HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return "ledger/ledgerForm";
        }
        HttpSession session = request.getSession(false);
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        ledgerService.saveLedger(ledgerFormDto, member.getId());
        return "redirect:/ledger/getList";
    }

    @GetMapping("/update")
    public String updateLedger(@RequestParam("ledgerId") Long ledgerId, Model model){
        LedgerFormDto ledgerFormDto = ledgerService.findLedger(ledgerId);
        model.addAttribute("ledgerFormDto", ledgerFormDto);
        return "ledger/ledgerForm";
    }

    @PostMapping("/update")
    public String updateLedger(@ModelAttribute LedgerFormDto ledgerFormDto, Model model, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        ledgerService.updateLedger(ledgerFormDto, member.getId());
        return "redirect:/ledger/getList";
    }

    @GetMapping("/getList")
    public String getLedgers(HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        TotalLedgerDto ledgers = ledgerService.getLedgers(member.getId());
        model.addAttribute("ledgers", ledgers);
        return "ledger/ledgerMng";
    }

    @PostMapping("/delete")
    public String deleteItem(@RequestParam("ledgerId") Long ledgerId, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        ledgerService.removeLedger(member.getId(),ledgerId);
        return "redirect:/ledger/getList";
    }


}
