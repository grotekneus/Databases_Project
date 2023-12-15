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
    private LocalDate year;

    public Console(String name, LocalDate year) {
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

    public LocalDate getYear() {
        return year;
    }

    public void setYear(LocalDate year) {
        this.year = year;
    }

}
