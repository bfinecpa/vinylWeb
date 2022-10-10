package project.vinyl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import org.jboss.jandex.Main;
import org.modelmapper.ModelMapper;
import project.vinyl.constant.ItemSellStatus;
import project.vinyl.entity.Item;
import project.vinyl.entity.ItemImg;

@Getter
@Setter
public class MainPageItemDto {

    private Long id;

    private String name;

    private int price;

    private String imgUrl;

    private ItemSellStatus itemSellStatus;

    @QueryProjection
    public MainPageItemDto(Long id, String name, int price, String imgUrl, ItemSellStatus itemSellStatus) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
        this.itemSellStatus = itemSellStatus;
    }
}
