package be.kuleuven.dbproject.domain;

import javax.persistence.*;

@Entity
public class GameInstance {
    //@ManyToOne
    //@JoinColumn
    @Column
    private int gameID;
    @Column
    @Id
    @GeneratedValue
    private int gameInstanceID;
    @Column
    private int museumID;

    public GameInstance(int gameID, int museumID) {
        this.gameID = gameID;
        this.museumID = museumID;
    }

    public int getGameID() {
        return gameID;
    }

    public int getGameInstanceID() {
        return gameInstanceID;
    }

    public int getMuseumID() {
        return museumID;
    }

    public void setMuseumID(int id){
        this.museumID = id;
    }
}
