package project.vinyl.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class WishItem {

    @Id
    @GeneratedValue
    @Column(name = "wish_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "wish_list_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private WishList wishList;

    public WishItem(Item item ,WishList wishList){
        this.item=item;
        this.wishList=wishList;
    }

}
