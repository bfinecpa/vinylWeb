package project.vinyl.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import project.vinyl.constant.ItemSellStatus;
import project.vinyl.entity.Item;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemDetailToDealDto {

    private Long id;

    private String name;

    private Long sellerId;

    private String details;

    private int price;

    private int stockNumber;

    private boolean negotiation;

    private ItemSellStatus itemSellStatus;

    private String tradingRate;

    private List<String> imgUrlList= new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemDetailToDealDto of(Item item, Long sellerId, String tradingRate,  List<String> itemImgUrlList){
        ItemDetailToDealDto itemDetailToDealDto = modelMapper.map(item, ItemDetailToDealDto.class);
        itemDetailToDealDto.setSellerId(sellerId);
        itemDetailToDealDto.setTradingRate(tradingRate);
        itemDetailToDealDto.setImgUrlList(itemImgUrlList);
        return itemDetailToDealDto;
    }

}
