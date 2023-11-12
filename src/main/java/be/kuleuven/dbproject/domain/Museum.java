package be.kuleuven.dbproject.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Museum {
    @Column
    private String address;
    @Column
    private int visitors;
    @Column
    @Id
    @GeneratedValue
    private int museumID;
    @Column
    private float revenue;
    @Column
    private String country;
}
