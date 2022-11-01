package project.vinyl.dto;

import lombok.Getter;
import project.vinyl.entity.Post;

import java.time.LocalDateTime;

@Getter
public class PostViewDto {
    private Long id;
    private String title;
    private String content;
    private String name;
    private int hits;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;

    public PostViewDto(Post entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.name = entity.getMember().getName();
        this.hits = entity.getHits();
        this.regTime = entity.getRegTime();
        this.updateTime = entity.getUpdateTime();
    }
}
