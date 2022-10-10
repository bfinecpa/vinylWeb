package project.vinyl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import project.vinyl.constant.ItemSellStatus;
import project.vinyl.entity.Item;

@Getter
@Setter
public class CRUDWishItemDto {

    private Long id;

    private String name;

    private int price;

    private boolean negotiation;

    private String imgUrl;

    private ItemSellStatus itemSellStatus;

    @QueryProjection
    public CRUDWishItemDto(Long id, String name, int price, boolean negotiation, String imgUrl, ItemSellStatus itemSellStatus) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.negotiation = negotiation;
        this.imgUrl = imgUrl;
        this.itemSellStatus = itemSellStatus;
    }
}
