package project.vinyl.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class TransactionDetails {

    @Id
    @GeneratedValue
    @Column(name = "transaction_details_id")
    private Long id;


    private Long itemId;

    private String itemName;

    private int price;

    private String itemImgUrl;

    private String sellerName;

    //메세지 룸으로 해야하나, 아니면 아이템 있는 애들 이거를 해야하나

    @ManyToOne
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Builder
    public TransactionDetails(String itemName,Long itemId ,int price, String itemImgUrl, String sellerName, Member member) {
        this.itemName = itemName;
        this.itemId = itemId;
        this.price = price;
        this.itemImgUrl = itemImgUrl;
        this.sellerName = sellerName;
        this.member = member;
    }
}
