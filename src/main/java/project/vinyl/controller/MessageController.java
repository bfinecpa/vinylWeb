package project.vinyl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import project.vinyl.entity.Member;
import project.vinyl.repository.MemberRepository;

import javax.annotation.PostConstruct;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MemberRepository memberRepository;

    @PostConstruct
    public void newMember(){
        Member member = new Member("asdf", "asdf", "asdf", "asdf", "123");
        memberRepository.save(member);
    }


    @GetMapping("/message")
    public String message(){
        return "message/messageService";
    }


}
