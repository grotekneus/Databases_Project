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
}
