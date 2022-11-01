package project.vinyl.dto;

import lombok.Getter;
import lombok.Setter;
import project.vinyl.entity.Comment;
import project.vinyl.entity.Member;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentViewDto {
    private Long id;
    private String comment;
    private Member member;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;

    public CommentViewDto(Comment entity) {
        this.id = entity.getId();
        this.comment = entity.getComment();
        this.member = entity.getMember();
        this.regTime = entity.getRegTime();
        this.updateTime = entity.getUpdateTime();
    }
}
