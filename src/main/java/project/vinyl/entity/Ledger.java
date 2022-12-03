package project.vinyl.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import project.vinyl.constant.BuySell;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Setter @Getter
public class Ledger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ledger_id")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Enumerated(EnumType.STRING)
    private BuySell buyOrSell;

    private int price;

    private String detail;

    @Builder
    public Ledger(String name, Member member,String detail, BuySell buyOrSell, int price) {
        this.name = name;
        this.member = member;
        this.detail = detail;
        this.buyOrSell = buyOrSell;
        this.price = price;
    }

    public void updateLedger(String name, String detail,BuySell buyOrSell, int price){
        this.name = name;
        this.detail = detail;
        this.buyOrSell = buyOrSell;
        this.price = price;
    }



}
