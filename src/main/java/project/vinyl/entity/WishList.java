package project.vinyl.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class WishList {

    @Id
    @GeneratedValue
    @Column(name = "wish_list_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public WishList(Member member){
        this.member = member;
    }

}
