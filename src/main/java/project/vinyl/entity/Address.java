package project.vinyl.entity;

import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
public class Address {

    private String zipcode;
    private String streetAdr;
    private String detailAdr;

    public Address(String zipcode, String streetAdr, String detailAdr) {
        this.zipcode = zipcode;
        this.streetAdr = streetAdr;
        this.detailAdr = detailAdr;
    }

}
