package project.vinyl.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class TransactionDetails {

    @Id
    @GeneratedValue
    @Column(name = "transaction_details_id")
    private Long id;

    //메세지 룸으로 해야하나, 아니면 아이템 있는 애들 이거를 해야하나
    @ManyToOne
    @JoinColumn(name = "message_room_id")
    @Nullable
    private MessageRoom messageRoom;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public TransactionDetails(MessageRoom messageRoom, Member member) {
        this.messageRoom = messageRoom;
        this.member = member;
    }
}
