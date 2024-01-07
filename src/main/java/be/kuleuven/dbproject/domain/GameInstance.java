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

    @Column
    @Id
    @GeneratedValue
    private int gameInstanceID;

    @ManyToOne
    @JoinColumn
    private Console console;

    public GameInstance(){
    }
    public GameInstance(Game game, Museum museum,Console console) {
        this.game = game;
        this.museum = museum;
        this.console = console;
    }

    public Console getConsole() {
        return console;
    }

    public void setConsole(Console console) {
        this.console = console;
    }

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
