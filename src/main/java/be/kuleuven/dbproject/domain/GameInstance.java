package be.kuleuven.dbproject.domain;

import javax.persistence.*;

@Entity
public class GameInstance {

    @ManyToOne
    @JoinColumn
    //@Column
    private Game game;
    @ManyToOne
    @JoinColumn
    private Museum museum;

    public GameInstance(){}

    public GameInstance(Game game, Museum museum) {
        this.game = game;
        this.museum = museum;
    }

    @Column
    @Id
    @GeneratedValue
    private int gameInstanceID;

    public Game getGame() {
        return game;
    }

    public Museum getMuseum() {
        return museum;
    }

    public void setMuseum(Museum museum) {
        this.museum = museum;
    }

    public int getGameInstanceID() {
        return gameInstanceID;
    }
}
