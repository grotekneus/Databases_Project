package be.kuleuven.dbproject.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Genre {
    @Column(name="genreId")
    @Id
    @GeneratedValue
    private int genreId;
    @Column
    private String description;

    @ManyToMany(mappedBy = "genres")
    private List<Game> games;
    public Genre(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGenreID() {
        return genreId;
    }

    public void addGame(Game g){
        this.games.add(g);
    }
}
