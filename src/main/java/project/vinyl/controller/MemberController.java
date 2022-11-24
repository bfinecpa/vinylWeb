package project.vinyl.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.vinyl.constant.SessionConst;
import project.vinyl.dto.AddMemberDto;
import project.vinyl.dto.LoginFormDto;
import project.vinyl.dto.PostFormDto;
import project.vinyl.entity.Member;
import project.vinyl.entity.Post;
import project.vinyl.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

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
            model.addAttribute("AddMemberDto", addMemberDto);

            // 유효성 검사를 통과하지 못 한 필드와 메시지 핸들링
            Map<String, String> errorMap = new HashMap<>();

            for(FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put("valid_"+error.getField(), error.getDefaultMessage());
                log.info("error message : "+error.getDefaultMessage());
            }

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

    @GetMapping("/edit")
    public String editForm(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        model.addAttribute("member", loginMember);
        log.info("member.name={}", loginMember.getName());

        return "members/editMemberForm";
    }

    @Transactional
    @PostMapping("/edit")
    public String edit(@ModelAttribute("member") AddMemberDto member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "members/editMemberForm";
        }
        log.info("c member.name={}", member.getName());
        memberService.modify(member);
        log.info("c member.name={}", member.getName());
        return "/myPage";
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

        return "redirect:/";
    }
}
