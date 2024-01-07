package be.kuleuven.dbproject.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Console {
    @Column
    private String name;
    @Column(name="consoleId")
    @Id
    @GeneratedValue
    private int consoleID;
    @Column
    private int year;

    @ManyToMany
    private List<Game> games;

    public Console(){

    }
    public Console(String name, int year, List<Game> games) {
        this.name = name;
        this.year = year;
        this.games = games;
    }

    public void addGame(Game g){
        this.games.add(g);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getConsoleID() {
        return consoleID;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
