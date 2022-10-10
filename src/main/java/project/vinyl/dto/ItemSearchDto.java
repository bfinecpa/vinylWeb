package project.vinyl.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {

    //private String searchBy; // 어떤것을 기준으로 검색을 할 것이냐?? 이다.

    private String searchQuery ="";
}
