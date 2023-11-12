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
}
