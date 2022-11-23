package project.vinyl.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.vinyl.constant.SessionConst;
import project.vinyl.dto.CommentFormDto;
import project.vinyl.dto.CommentViewDto;
import project.vinyl.entity.Comment;
import project.vinyl.entity.Member;
import project.vinyl.entity.Post;
import project.vinyl.service.CommentService;
import project.vinyl.service.PostService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping()
public class CommentController {
    private final PostService postService;
    private final CommentService commentService;

    @RequestMapping("/comment/register")
    public String register(Model model, @RequestParam Map<String, Object> param, HttpServletRequest request) {
        CommentFormDto commentFormDto = new CommentFormDto();
        commentFormDto.setComment(param.get("comment").toString());

        HttpSession session = request.getSession();
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        Long postId = Long.parseLong(param.get("postId").toString());
        Post post = postService.getPost(postId);

        commentService.register(commentFormDto, post,loginMember);

        List<CommentViewDto> commentList = commentService.getList(post);

        // 작성자가 글을 조회하면 수정/삭제 버튼 보임
        model.addAttribute("loginMember", loginMember);

        // 댓글이 있는 경우에만 댓글창 보임
        if (!commentList.isEmpty()) {
            model.addAttribute("commentExist", true);
            model.addAttribute("commentList", commentList);
            model.addAttribute("post", post);
        }

//        String redirect = "redirect:/post/" + postId;
//        return redirect;
        return "post/commentList";
    }

//    @PostMapping("/comment/{postId}/register")
//    public String save(@PathVariable Long postId, @Valid @ModelAttribute CommentFormDto commentFormDto,
//                       BindingResult bindingResult, HttpServletRequest request) {
//        if (bindingResult.hasErrors()) {
//            return "post/{postId}";
//        }
//
//        HttpSession session = request.getSession();
//        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
//        Post post = postService.getPost(postId);
//        commentService.register(commentFormDto, post,loginMember);
//
//        return "redirect:/post/{postId}";
//    }

    @PostMapping("/comment/{commentId}/delete/{postId}")
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

    @PutMapping({"/comment/{commentId}"})
    public ResponseEntity update(@PathVariable Long commentId, @RequestBody CommentViewDto dto) {
        commentService.modify(commentId, dto);
        return ResponseEntity.ok(commentId);
    }}
