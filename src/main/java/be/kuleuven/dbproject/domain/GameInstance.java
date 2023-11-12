package be.kuleuven.dbproject.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class GameInstance {
    @Column
    private int gameID;
    @Column
    @Id
    @GeneratedValue
    private int gameInstanceID;
    @Column
    private int museumID;
}
