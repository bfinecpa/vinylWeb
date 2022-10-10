package project.vinyl.entity;

import lombok.Getter;
import project.vinyl.constant.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;


    //member가 사라지면 올린 아이템도 사라져야 한다.

    @OneToMany(mappedBy = "member", orphanRemoval = true ,fetch = FetchType.LAZY)
    private List<Item> item = new ArrayList<>();


}
