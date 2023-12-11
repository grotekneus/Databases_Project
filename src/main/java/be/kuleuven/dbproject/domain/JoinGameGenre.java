package be.kuleuven.dbproject.domain;

import javax.persistence.*;
import javax.persistence.OneToOne;
@Entity
public class JoinGameGenre {
    @Id
    @Column
    public int gameID;

    @Id
    @Column
    public int genreID;
}
