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
    @PrePersist
    private void prePersist() {
        System.out.println("Bezig met het bezigen van het opslaan van student " + this);
    }

}
