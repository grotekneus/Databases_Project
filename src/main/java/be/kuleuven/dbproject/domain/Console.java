package be.kuleuven.dbproject.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Console {
    @Column
    private String name;
    @Column
    @Id
    @GeneratedValue
    private int consoleID;
    @Column
    private int year;

    public Console(){}

    public Console(String name, int year) {
        this.name = name;
        this.year = year;
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
