package be.kuleuven.dbproject.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Loan {
    @Column
    @Id
    private int gameID;
    @Column
    private int customerID;
    @Column
    private LocalDate date;
    @Column
    private LocalDate returned;

    public Loan(LocalDate date, LocalDate returned) {
        this.date = date;
        this.returned = returned;
    }

    public int getGameID() {
        return gameID;
    }

    public int getCustomerID() {
        return customerID;
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
