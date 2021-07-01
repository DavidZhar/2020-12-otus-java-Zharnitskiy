package ru.otus.crm.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String street;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private Client client;

    public Address(String street) {
        this.street = street;
    }

    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }

    @Override
    public Address clone() {
        return new Address(this.id, this.street, this.client);
    }

    @Override
    public String toString() {
        return "AddressDataSet{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}
