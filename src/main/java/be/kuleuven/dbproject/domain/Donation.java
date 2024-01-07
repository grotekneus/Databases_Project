package be.kuleuven.dbproject.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Donation   {

    @ManyToOne//???????????????
    @JoinColumn
    private Customer customer;
    @Column
    private int customerId;

    @Id
    @Column
    @GeneratedValue
    private int donationID;


    @Column
    private LocalDate date;


    @Column
    private float moneyDonated;

    public Donation(){

    }
    public Donation(float moneyDonated, LocalDate date, int customerId) {
        this.moneyDonated = moneyDonated;
        this.date = date;
        this.customerId = customerId;
    }

    public Donation(float moneyDonated, LocalDate date,Customer customer) {
        this.moneyDonated = moneyDonated;
        this.date = date;
        this.customer = customer;

    }

    public float getMoneyDonated() {
        return moneyDonated;
    }

    public int getCustomerId(){return customerId;}

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
