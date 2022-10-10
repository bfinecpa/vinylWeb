package project.vinyl.entity;

import lombok.Getter;
import lombok.Setter;
import project.vinyl.constant.ItemSellStatus;
import project.vinyl.dto.ItemFormDto;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Item extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Lob
    private String details;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockNumber;

    private boolean negotiation;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
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
