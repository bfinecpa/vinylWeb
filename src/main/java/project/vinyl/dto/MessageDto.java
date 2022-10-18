package project.vinyl.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {

    private Long id;

    private Long roomId;

    private String content;

    private Long sendMemberId;

    private Long receiveMemberId;

    private String sendMemberName;

    private String receiveMemberName;

    public MessageDto(Long id, Long roomId, String content, Long sendMemberId, Long receiveMemberId, String sendMemberName, String receiveMemberName) {
        this.id = id;
        this.roomId = roomId;
        this.content = content;
        this.sendMemberId = sendMemberId;
        this.receiveMemberId = receiveMemberId;
        this.sendMemberName = sendMemberName;
        this.receiveMemberName = receiveMemberName;
    }
}
