package project.vinyl.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class TotalLedger {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne (cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    private int income =0;

    private int expense =0;

    public TotalLedger(Member member) {
        this.member = member;
    }
}
