package project.vinyl.dto;


import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import project.vinyl.constant.ItemSellStatus;
import project.vinyl.entity.Item;
import project.vinyl.entity.ItemImg;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CRUDItemDto {

    private Long id;

    private String name;

    private int price;

    private int stockNumber;

    private boolean negotiation;

    private String imgUrl;

    private ItemSellStatus itemSellStatus;

    private static ModelMapper modelMapper = new ModelMapper();

    public static CRUDItemDto of(Item item){
        return modelMapper.map(item, CRUDItemDto.class);
    }
}
