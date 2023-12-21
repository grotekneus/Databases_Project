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

    public Loan(Customer customer, LocalDate date, LocalDate returned, Game game) {
        this.customer = customer;
        this.date = date;
        this.returned = returned;
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public Customer getCustomer() {
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

    public void setGame(Game game) {
        this.game = game;
    }

}
