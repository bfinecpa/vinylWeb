package project.vinyl.entity;

import lombok.Getter;
import lombok.Setter;
import project.vinyl.constant.Role;
import project.vinyl.dto.AddMemberDto;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String loginId;
    private String password;
    private String name;
    private String email;

    @Embedded
    private Address address;
    private String phoneNumber;

    private Double tradingRate =3.0;

    private Long countRatingPerson = 1L;

    public Member(){

    }

    public Member(String loginId, String password, String name, String email, Address address, String phoneNumber) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public static Member createMember(AddMemberDto addMemberDto) {
        Member member = new Member();
        member.setLoginId(addMemberDto.getLoginId());
        member.setPassword(addMemberDto.getPassword());
        member.setName(addMemberDto.getName());
        member.setEmail(addMemberDto.getEmail());
        member.setAddress(new Address(addMemberDto.getZipcode(), addMemberDto.getStreetAdr(), addMemberDto.getDetailAdr()));
        member.setPhoneNumber(addMemberDto.getPhoneNumber());
        return member;
    }

    public void update(AddMemberDto addMemberDto) {
        this.password = addMemberDto.getPassword();
        this.name = addMemberDto.getName();
        this.phoneNumber = addMemberDto.getPhoneNumber();
    }

    public void updatePassword(String password){
        this.password = password;
    }
    //member가 사라지면 올린 아이템도 사라져야 한다.

    public void updateRating(Double rate){
        countRatingPerson +=1;
        tradingRate+=rate;
    }

}
