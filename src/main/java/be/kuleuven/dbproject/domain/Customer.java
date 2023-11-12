package be.kuleuven.dbproject.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Customer {
    @Column
    @Id
    @GeneratedValue
    private int customerID;
    @Column
    private String fullName;
    @Column
    private String address;
    @Column
    private String email;
}
