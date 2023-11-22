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

    public Game(String name, int year, float value) {
        this.name = name;
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

    public int getConsoleID() {
        return consoleID;
    }

    public int getGenreID() {
        return genreID;
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
