package be.kuleuven.dbproject.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Game {
    @Column
    private String name;

    @ManyToOne
    @JoinColumn
    private Console console;

    @ManyToOne
    @JoinColumn

    private Genre genre;
    @Column
    private int year;
    @Column
    @Id
    @GeneratedValue
    private int gameID;
    @Column
    private float value;

    public Game(String name, int year, float value) {
        this.name = name;
        this.year = year;
        this.value = value;
    }

    public Game(String name, Console console, Genre genre, int year, float value) {
        this.name = name;
        this.console = console;
        this.genre = genre;
        this.year = year;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public Console getConsole() {
        return console;
    }

    public Genre getGenre() {
        return genre;
    }

    public int getGameID() {
        return gameID;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
