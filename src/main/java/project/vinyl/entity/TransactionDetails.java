package project.vinyl.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @OneToOne
    private MessageRoom messageRoom;

    @ElementCollection
    @CollectionTable(
        name = "member_complete",
            joinColumns = @JoinColumn(name = "transaction_details_id")
    )
    @MapKeyColumn(name = "member_id")
    @Column(name = "completeTF")
    private Map<Long, Boolean> transactionCompleted = new HashMap<>();

    public TransactionDetails(MessageRoom messageRoom) {
        this.messageRoom = messageRoom;
    }
}
