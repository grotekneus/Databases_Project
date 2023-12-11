package be.kuleuven.dbproject.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Donation   {


    @ManyToOne
    @JoinColumn
    private Customer customer;
    @Id
    @Column
    private float moneyDonated;
    @Column
    private LocalDate date;

    public Donation(float moneyDonated, LocalDate date) {
        this.moneyDonated = moneyDonated;
        this.date = date;
    }

    public float getMoneyDonated() {
        return moneyDonated;
    }

    public void setMoneyDonated(float moneyDonated) {
        this.moneyDonated = moneyDonated;
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
}
