package project.vinyl.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import project.vinyl.constant.ItemSellStatus;
import project.vinyl.dto.ItemFormDto;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Item extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Lob
    private String details;

    @Builder
    public Item(String name, String details, int price, int stockNumber, boolean negotiation, ItemSellStatus itemSellStatus, Member member) {
        this.name = name;
        this.details = details;
        this.price = price;
        this.stockNumber = stockNumber;
        this.negotiation = negotiation;
        this.itemSellStatus = itemSellStatus;
        this.member = member;
    }

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockNumber;

    private boolean negotiation;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    public void updateItem(ItemFormDto itemFormDto) {
        this.name=itemFormDto.getName();
        this.details=itemFormDto.getDetails();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.negotiation = itemFormDto.isNegotiation();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

}
