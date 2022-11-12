package project.vinyl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDetailsDto {

    private Long id;

    private String itemName;

    private int price;

    private String itemImgUrl;

    private String sellerName;

    public TransactionDetailsDto(Long id, String itemName, int price, String itemImgUrl, String sellerName) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.itemImgUrl = itemImgUrl;
        this.sellerName = sellerName;
    }
}
