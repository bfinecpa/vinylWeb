package project.vinyl.dto;

import lombok.Getter;
import lombok.Setter;
import project.vinyl.entity.Member;
import project.vinyl.entity.Post;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class PostFormDto {
    @NotEmpty(message = "제목을 입력하세요")
    private String title;
    @NotEmpty(message = "내용을 입력하세요")
    private String content;

    public Post toEntity(Member member) {
        return Post.builder()
                .title(title)
                .content(content)
                .member(member)
                .build();
    }
}
