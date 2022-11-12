package project.vinyl.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ComparePriceDto implements Comparable<ComparePriceDto>{

    private String imgUrl;

    private String link;

    private String name;

    private String price;

    private boolean available;

    @Builder
    public ComparePriceDto(String imgUrl, String link, String name, String price, boolean available) {
        this.imgUrl = imgUrl;
        this.link = link;
        this.name = name;
        this.price = price;
        this.available = available;
    }

    @Override
    public int compareTo(ComparePriceDto comparePriceDto) {
        if(Integer.parseInt(comparePriceDto.price)< Integer.parseInt(price)){
            return 1;
        }else if(Integer.parseInt(comparePriceDto.price)> Integer.parseInt(price)){
            return -1;
        }
        return 0;
    }
}
