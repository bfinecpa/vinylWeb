package project.vinyl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.vinyl.dto.CommentFormDto;
import project.vinyl.dto.CommentViewDto;
import project.vinyl.dto.PostFormDto;
import project.vinyl.entity.Comment;
import project.vinyl.entity.Member;
import project.vinyl.entity.Post;
import project.vinyl.repository.CommentRepository;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment register(CommentFormDto commentFormDto, Post post, Member member){
        Comment comment = commentFormDto.toEntity(post, member);
        return commentRepository.save(comment);
    }

    public List<CommentViewDto> getList(Post post) {
        List<Comment> list= commentRepository.findAllByPost(post);
        return list.stream().map(CommentViewDto::new).collect(Collectors.toList());
    }

    public Comment getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new EntityExistsException("존재하지 않는 댓글입니다."));
        return comment;
    }

    public void delete(Long commentId){
        Comment comment = getComment(commentId);
        commentRepository.delete(comment);
    }

    public Comment modify(Long commentId, CommentViewDto commentViewDto) {
        Comment comment = getComment(commentId);
        comment.update(commentViewDto.getComment());
        return comment;
    }
}
