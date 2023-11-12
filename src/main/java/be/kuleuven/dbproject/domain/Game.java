package be.kuleuven.dbproject.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Game {
    @Column
    private String name;
    @Column
    private int consoleID;
    @Column
    private int genreID;
    @Column
    private int year;
    @Column
    @Id
    @GeneratedValue
    private int gameID;
    @Column
    private float value;
}
