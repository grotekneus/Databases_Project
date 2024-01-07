package be.kuleuven.dbproject.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Loan {
    @ManyToOne
    @JoinColumn
    private GameInstance gameinstance;
    @ManyToOne
    @JoinColumn
    private Customer customer;
    @Id
    @Column
    private LocalDate date;

    @Column
    private LocalDate returned;
    public Loan(){

    }
    public Loan(Customer customer, LocalDate date, LocalDate returned, GameInstance gameinstance) {
        this.customer = customer;
        this.date = date;
        this.returned = returned;
        this.gameinstance = gameinstance;
    }

    public GameInstance getGameInstance() {
        return gameinstance;
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

    public void setGameInstance(GameInstance gameinstance) {
        this.gameinstance = gameinstance;
    }

}
