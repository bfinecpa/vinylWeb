package project.vinyl.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;
    @Column(nullable = false)
    private String content;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private int hits = 0;

    @Builder
    public Post(String title, String content, Member member, int hits){
        this.title = title;
        this.content = content;
        this.member = member;
        this.hits = hits;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void hitCnt() {
        this.hits++;
    }
}