package be.kuleuven.dbproject.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Donation   {
    @Column
    @Id
    private int customerID;
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

    public int getCustomerID() {
        return customerID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
