package project.vinyl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import project.vinyl.entity.Item;
import project.vinyl.entity.TransactionDetails;

@Getter
@Setter
public class TransactionDetailsDto {

    private Long id;

    private String itemName;

    private int price;

    private String itemImgUrl;

    private String sellerName;

    @Builder
    public TransactionDetailsDto(Long id, String itemName, int price, String itemImgUrl, String sellerName) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.itemImgUrl = itemImgUrl;
        this.sellerName = sellerName;
    }


}
