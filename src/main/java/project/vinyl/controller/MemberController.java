package project.vinyl.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.vinyl.constant.SessionConst;
import project.vinyl.dto.AddMemberDto;
import project.vinyl.dto.LoginFormDto;
import project.vinyl.entity.Member;
import project.vinyl.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/add")
    public String addForm(@ModelAttribute("addMemberDto") AddMemberDto addMemberDto) {
        return "members/addMemberForm";
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute AddMemberDto addMemberDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "members/addMemberForm";
        }
        try {
            memberService.join(addMemberDto);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "members/addMemberForm";
        }

        return "redirect:/login";
    }
    @GetMapping("/delete")
    public String delete(@ModelAttribute("loginFormDto") LoginFormDto loginFormDto) {
        return "members/deleteMemberForm";
    }

    @Transactional
    @PostMapping("/delete")
    public String delete(@Valid @ModelAttribute LoginFormDto form, BindingResult bindingResult, HttpServletRequest request){
        if (bindingResult.hasErrors()) {
            log.info("return");
            return "members/deleteMemberForm";
        }

        HttpSession session = request.getSession();
        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);

        Member deleteMember = memberService.check(form.getLoginId(), form.getPassword());

        if (deleteMember == null) {
            bindingResult.reject("loginFail", "아이디 혹은 비밀번호를 정확하게 입력하세요.");
            return "members/deleteMemberForm";
        }

        if (loginMember.getLoginId() == deleteMember.getLoginId()) {
            memberService.delete(deleteMember);
            if (session != null) {
                session.invalidate();
            }
        }
        else {
            bindingResult.reject("checkFail", "현재 로그인한 회원이 아닙니다.");
            return "members/deleteMemberForm";
        }

        return "redirect:/home";
    }
}
