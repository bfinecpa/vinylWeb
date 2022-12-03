package project.vinyl.dto;

import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;
import project.vinyl.constant.ItemSellStatus;
import project.vinyl.entity.Item;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수입니다.")
    private String name;

    @NotNull(message = "상세 설명은 필수입니다.")
    private String details;


    //NotBlank는 String만 가능
    @NotNull(message = "가격은 필수입니다.")
    private int price;

    @NotNull(message = "수량은 필수입니다.")
    private int stockNumber;

    @NotNull(message = "네고 여부는 필수 입니다.")
    private boolean negotiation;

    private ItemSellStatus itemSellStatus;

    // 수정시 이미지 데이터를 갖고 있는 리스트 저장시에는 값 없음
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    @Builder
    public ItemFormDto(Long id, String name, String details, int price, int stockNumber, boolean negotiation, ItemSellStatus itemSellStatus) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.price = price;
        this.stockNumber = stockNumber;
        this.negotiation = negotiation;
        this.itemSellStatus = itemSellStatus;
    }

//    // 수정시 데이터 바꿀때 좀 더 편하게 하려고 있는 리스트
//    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item, List<ItemImgDto> itemImgDtoList){
        ItemFormDto itemFormDto = modelMapper.map(item, ItemFormDto.class);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

}
