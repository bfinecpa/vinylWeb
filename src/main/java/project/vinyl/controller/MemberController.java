package project.vinyl.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import project.vinyl.dto.AddMemberDto;
import project.vinyl.entity.Member;
import project.vinyl.service.MemberService;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/add")
    public String addForm(@ModelAttribute("addMemberDto") AddMemberDto addMemberDto) {
        return "members/addMemberForm";
    }

    @PostMapping("/members/add")
    public String save(@Valid @ModelAttribute AddMemberDto addMemberDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "members/addMemberForm";
        }
        Member member = Member.createMember(addMemberDto);
        memberService.join(member);
        return "redirect:/home";
    }
}
