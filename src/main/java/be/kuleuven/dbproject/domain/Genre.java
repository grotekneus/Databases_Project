package be.kuleuven.dbproject.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Genre {
    @Column
    @Id
    @GeneratedValue
    private int GenreID;
    @Column
    private String description;

}
