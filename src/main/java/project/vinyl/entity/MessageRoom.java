package project.vinyl.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MessageRoom {

    @Id
    @GeneratedValue
    @Column(name = "message_room_id")
    private Long id;


    @ManyToOne
    @JoinColumn(name = "item_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "participate_member1")
    private Member participateMember1;

    @ManyToOne
    @JoinColumn(name = "participate_member2")
    private Member participateMember2;

    @Builder
    public MessageRoom(Item item, Member participateMember1, Member participateMember2) {
        this.item = item;
        this.participateMember1 = participateMember1;
        this.participateMember2 = participateMember2;
    }
}
