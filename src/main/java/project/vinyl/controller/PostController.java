package project.vinyl.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.vinyl.constant.SessionConst;
import project.vinyl.dto.CommentFormDto;
import project.vinyl.dto.CommentViewDto;
import project.vinyl.dto.PostFormDto;
import project.vinyl.dto.PostViewDto;
import project.vinyl.entity.Member;
import project.vinyl.entity.Post;
import project.vinyl.service.CommentService;
import project.vinyl.service.PostService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;

    @GetMapping
    public String list(Model model, @PageableDefault(size=15, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostViewDto> postList = postService.getList(pageable);
        model.addAttribute("postList", postList);
        model.addAttribute("nowPage", postList.getPageable().getPageNumber());
        model.addAttribute("totalPages", postList.getTotalPages());
        return "post/list";
    }

    @GetMapping("/{postId}")
    public String getPost(@PathVariable Long postId, @ModelAttribute("commentFormDto") CommentFormDto commentFormDto, HttpServletRequest request,
                          Model model, RedirectAttributes redirectAttributes){
        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        try {
            Post post = postService.getPost(postId);
            List<CommentViewDto> commentList = commentService.getList(post);

            // ???????????? ?????? ???????????? ????????? ???????????? ??????, ??????/?????? ?????? ??????
            if (loginMember.getId() != post.getMember().getId()) postService.hitsInc(post);
            model.addAttribute("loginMember", loginMember);

            // ????????? ?????? ???????????? ????????? ??????
            if (!commentList.isEmpty()) {
                model.addAttribute("commentExist", true);
                model.addAttribute("commentList", commentList);
            }

            model.addAttribute("post", post);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/post";
        }
        return "post/getPost";
    }


    @GetMapping("/register")
    public String addForm(@ModelAttribute("postFormDto") PostFormDto postFormDto) {
        return "post/registerPostForm";
    }

    @PostMapping("/register")
    public String save(@Valid @ModelAttribute PostFormDto postFormDto, BindingResult bindingResult,
                       Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "post/registerPostForm";
        }

        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        try {
            Post post = postService.register(postFormDto, loginMember);
            redirectAttributes.addAttribute("postId", post.getId());
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "post/registerPostForm";
        }

        return "redirect:/post/{postId}";
    }

    @GetMapping("/{postId}/edit")
    public String editForm(@PathVariable Long postId, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        try {
            Post post = postService.getPost(postId);

            if (loginMember.getId() != post.getMember().getId()) {
                redirectAttributes.addFlashAttribute("errorMessage", "???????????? ????????? ????????? ?????? ????????? ??? ????????????.");
                return "redirect:/post/{postId}";
            }

            model.addAttribute("post", post);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/post";
        }

        return "post/editPostForm";
    }

    @PostMapping("/{postId}/edit")
    public String edit(@PathVariable Long postId, @ModelAttribute("post") PostFormDto postFormDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "post/editPostForm";
        }
        postService.modify(postId, postFormDto);
        return "redirect:/post/{postId}";
    }

    @GetMapping("/{postId}/delete")
    public String invalidDelete(@PathVariable Long postId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "???????????? ????????? ????????? ?????? ????????? ??? ????????????.");
        return "redirect:/post/{postId}";
    }


    @PostMapping("/{postId}/delete")
    public String delete(@PathVariable Long postId, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        try {
            Post post = postService.getPost(postId);

            if (loginMember.getId() != post.getMember().getId()) {
                redirectAttributes.addFlashAttribute("errorMessage", "???????????? ????????? ????????? ?????? ????????? ??? ????????????.");
                return "redirect:/post/{postId}";
            }

            postService.delete(postId);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/post";
    }

    @GetMapping("/myPost")
    public String myPost(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        List<PostViewDto> postList = postService.myPost(loginMember.getId());
        model.addAttribute("postList", postList);
        return "post/myPost";
    }
}