package project.vinyl.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.vinyl.constant.SessionConst;
import project.vinyl.entity.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    @GetMapping("/home")
    public String homeLogin(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home";
        }

        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);

        if (member == null) {
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";

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

        model.addAttribute("member", member);
        return "myPage";

    }
}
