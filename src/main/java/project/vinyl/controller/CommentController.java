package project.vinyl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.vinyl.constant.SessionConst;
import project.vinyl.dto.CommentFormDto;
import project.vinyl.entity.Comment;
import project.vinyl.entity.Member;
import project.vinyl.entity.Post;
import project.vinyl.service.CommentService;
import project.vinyl.service.PostService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final PostService postService;
    private final CommentService commentService;

    @PostMapping("/{postId}/register")
    public String save(@PathVariable Long postId, @Valid @ModelAttribute CommentFormDto commentFormDto,
                       BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "post/{postId}";
        }

        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        Post post = postService.getPost(postId);
        commentService.register(commentFormDto, post,loginMember);

        return "redirect:/post/{postId}";
    }

    @PostMapping("/{commentId}/delete/{postId}")
    public String delete(@PathVariable Long commentId, @PathVariable Long postId,
                         HttpServletRequest request, RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        try {
            Comment comment = commentService.getComment(commentId);

            if (loginMember.getId() != comment.getMember().getId()) {
                redirectAttributes.addFlashAttribute("errorMessage", "댓글을 작성한 회원만 글을 삭제할 수 있습니다.");
                return "redirect:/post/{postId}";
            }

            commentService.delete(commentId);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/post/{postId}";
    }
}
