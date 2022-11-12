package project.vinyl.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchDto {

    private String itemLink;
    private String imgLink;
    private String name;
    private int price;

    @Builder
    public SearchDto(String itemLink, String imgLink, String name, int price) {
        this.itemLink = itemLink;
        this.imgLink = imgLink;
        this.name = name;
        this.price = price;
    }
}
