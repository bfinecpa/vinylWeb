package project.vinyl.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import project.vinyl.constant.BuySell;
import project.vinyl.entity.Ledger;

@Getter @Setter
@NoArgsConstructor
public class LedgerFormDto {

    private Long id;

    private String name;

    private String detail;

    private BuySell buyOrSell;

    private int price;

    private static ModelMapper modelMapper = new ModelMapper();

    public LedgerFormDto(Long id, String name, String detail, BuySell buyOrSell, int price) {
        this.id = id;
        this.name = name;
        this.detail = detail;
        this.buyOrSell = buyOrSell;
        this.price = price;
    }

    public static LedgerFormDto of(Ledger ledger){return modelMapper.map(ledger, LedgerFormDto.class);}
}
