package project.vinyl.dto;

import lombok.Getter;
import lombok.Setter;
import project.vinyl.entity.Comment;
import project.vinyl.entity.Member;
import project.vinyl.entity.Post;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CommentFormDto {
    @NotEmpty(message = "댓글 내용을 입력하세요")
    private String comment;

    public Comment toEntity(Post post, Member member) {
        return Comment.builder()
                .comment(comment)
                .post(post)
                .member(member)
                .build();
    }
}
