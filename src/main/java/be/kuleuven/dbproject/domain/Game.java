package be.kuleuven.dbproject.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
public class Game {
    @Column
    private String name;

    @ManyToMany
    @JoinTable(
            name="games_consoles",
            joinColumns=@JoinColumn(name="gameId"),
            inverseJoinColumns=@JoinColumn(name="consoleId"))
    private List<Console> consoles;

    @ManyToMany
    @JoinTable(
            name="games_genres",
            joinColumns=@JoinColumn(name="gameId"),
            inverseJoinColumns=@JoinColumn(name="genreId"))
    private List<Genre> genres;
    @Column
    private int year;
    @Column(name="gameID")
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

    public Game(){

    }
    public Game(String name, List<Console> consoles, List<Genre> genres, int year, float value) {
        this.name = name;
        this.consoles = consoles;
        this.genres = genres;
        this.year = year;
        this.value = value;
    }

    public Game(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }
    public void addConsole(Console c){
        this.consoles.add(c);
    }
    public List<Console> getConsoles() {
        return consoles;
    }

    public List<Genre> getGenres() {
        return genres;
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

    public void addGenre(Genre genre){
        this.genres.add(genre);
    }

    public void setValue(float value) {
        this.value = value;
    }
}

