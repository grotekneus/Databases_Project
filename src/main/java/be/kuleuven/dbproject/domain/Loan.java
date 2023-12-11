package be.kuleuven.dbproject.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Loan {
    @ManyToOne
    @JoinColumn
    private Game game;
    @ManyToOne
    @JoinColumn
    private Customer customer;
    @Id
    @Column
    private LocalDate date;
    @Column
    private LocalDate returned;

    public Loan(LocalDate date, LocalDate returned) {
        this.date = date;
        this.returned = returned;
    }

    public Game getGame() {
        return game;
    }

    public Customer getCustomerID() {
        return customer;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getReturned() {
        return returned;
    }

    public void setReturned(LocalDate returned) {
        this.returned = returned;
    }
}
