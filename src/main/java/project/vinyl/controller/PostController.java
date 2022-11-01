package project.vinyl.controller;

import lombok.RequiredArgsConstructor;
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

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;

    @GetMapping
    public String list(Model model) {
        List<PostViewDto> postList = postService.getList();
        model.addAttribute("postList", postList);
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

            // 작성자가 글을 조회하면 조회수 증가하지 않음, 수정/삭제 버튼 보임
            if (loginMember.getId() != post.getMember().getId()) postService.hitsInc(post);
            model.addAttribute("loginMember", loginMember);

            // 댓글이 있는 경우에만 댓글창 보임
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
                redirectAttributes.addFlashAttribute("errorMessage", "게시글을 작성한 회원만 글을 수정할 수 있습니다.");
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

    @PostMapping("/{postId}/delete")
    public String delete(@PathVariable Long postId, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        try {
            Post post = postService.getPost(postId);

            if (loginMember.getId() != post.getMember().getId()) {
                redirectAttributes.addFlashAttribute("errorMessage", "게시글을 작성한 회원만 글을 삭제할 수 있습니다.");
                return "redirect:/post/{postId}";
            }

            postService.delete(postId);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/post";
    }
}