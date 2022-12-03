package project.vinyl.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "send_member_id")
    private Member sendMember;

    @ManyToOne
    @JoinColumn(name = "receive_member_id")
    private Member receiveMember;

    @ManyToOne
    @JoinColumn(name = "messageRoom_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MessageRoom messageRoom;

    private LocalDateTime sendTime;

    private String content;

    @Builder
    public Message(Member sendMember, Member receiveMember, MessageRoom messageRoom, String content) {
        this.sendMember = sendMember;
        this.receiveMember = receiveMember;
        this.messageRoom = messageRoom;
        this.content = content;
    }
}
