package project.vinyl.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRoomDto {

    private Long id;

    private String itemName;

    public MessageRoomDto(Long id, String itemName) {
        this.id = id;
        this.itemName = itemName;
    }
}
