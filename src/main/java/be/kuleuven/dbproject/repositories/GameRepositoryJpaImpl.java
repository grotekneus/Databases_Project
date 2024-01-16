package be.kuleuven.dbproject.repositories;

import be.kuleuven.dbproject.domain.*;

import javax.persistence.EntityManager;
import java.util.List;

public class GameRepositoryJpaImpl {
    private final EntityManager entityManager;

    public GameRepositoryJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public List<Game> getAllGames() {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Game.class);
        var root = query.from(Game.class);
        return entityManager.createQuery(query).getResultList();
    }

    public List<GameInstance> getAllGameInstances(Game game) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(GameInstance.class);
        var root = query.from(GameInstance.class);
        query.where(criteriaBuilder.equal(root.get("game"),game));
        return entityManager.createQuery(query).getResultList();
    }

    public String[] getAllGameInstanceNames() {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(GameInstance.class);
        var root = query.from(GameInstance.class);
        List<GameInstance> Instances = entityManager.createQuery(query).getResultList();
        String[] results = new String[Instances.size()];
        for (int i = 0; i < Instances.size(); i++) {
            results[i] = String.valueOf(Instances.get(i).getGameInstanceID());
        }
        return results;
    }

    public List<GameInstance> getGameInstancesByGameAndMuseum(Game game, Museum museum) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(GameInstance.class);
        var root = query.from(GameInstance.class);
        query.where(
                criteriaBuilder.equal(root.get("game"), game),
                criteriaBuilder.equal(root.get("museum"), museum)
        );
        return entityManager.createQuery(query).getResultList();
    }

    public List<GameInstance> getAllGameInstancesBasedOnMuseum(Museum museum) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(GameInstance.class);
        var root = query.from(GameInstance.class);
        query.where(criteriaBuilder.equal(root.get("museum"),museum));
        return entityManager.createQuery(query).getResultList();
    }

    public void addGame(Game g) {
        entityManager.getTransaction().begin();
        entityManager.persist(g);
        entityManager.getTransaction().commit();
    }
    public void addGameInstance(GameInstance gInstance) {
        entityManager.getTransaction().begin();
        entityManager.persist(gInstance);
        entityManager.getTransaction().commit();
    }

    public void changeGameValue(int value,Game g) {
        entityManager.getTransaction().begin();
        g.setValue(value);
        entityManager.merge(g);
        entityManager.getTransaction().commit();
    }

        public void changeGameInstanceMuseum(Museum museum, GameInstance selectedGInstance) {
        entityManager.getTransaction().begin();
        selectedGInstance.setMuseum(museum);
        entityManager.merge(selectedGInstance);
        entityManager.getTransaction().commit();
    }

    public Game findGameByName(String name) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Game.class);
        var root = query.from(Game.class);
        query.where(criteriaBuilder.equal(root.get("name"),name));
        return entityManager.createQuery(query).getSingleResult();
    }

    public String[] getAllGameNames(){
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Game.class);
        var root = query.from(Game.class);
        var list = entityManager.createQuery(query).getResultList();
        String[] results = new String[list.size()];
        for(int i = 0; i< list.size(); i++){
            results[i] = list.get(i).getName();
        }
        return results;
    }

    public GameInstance findGameInstanceByID(Integer id) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(GameInstance.class);
        var root = query.from(GameInstance.class);
        query.where(criteriaBuilder.equal(root.get("gameInstanceID"),id));
        return entityManager.createQuery(query).getSingleResult();
    }

    public List<Genre> getGameGenres(Game selectedGame) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Game.class);
        var root = query.from(Game.class);
        query.where(criteriaBuilder.equal(root.get("gameID"),selectedGame.getGameID()));
        return entityManager.createQuery(query).getSingleResult().getGenres();
    }

    public void addGenreToGame(Game selectedGame, Genre genre) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Game.class);
        var root = query.from(Game.class);
        query.where(criteriaBuilder.equal(root.get("gameID"),selectedGame.getGameID()));
        if(!selectedGame.getGenres().contains(genre)){
            entityManager.getTransaction().begin();
            selectedGame.addGenre(genre);
            genre.addGame(selectedGame);
            entityManager.getTransaction().commit();
        }
    }

    public void changeGameInstanceConsole(GameInstance selectedGInstance, Console console) {
        entityManager.getTransaction().begin();
        selectedGInstance.setConsole(console);
        entityManager.getTransaction().commit();
    }

    public List<Console> getGameConsoles(Game selectedGame) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Game.class);
        var root = query.from(Game.class);
        query.where(criteriaBuilder.equal(root.get("gameID"),selectedGame.getGameID()));
        return entityManager.createQuery(query).getSingleResult().getConsoles();
    }

    public void addConsoleToGame(Game selectedGame, Console console) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Game.class);
        var root = query.from(Game.class);
        query.where(criteriaBuilder.equal(root.get("gameID"),selectedGame.getGameID()));
        if(!selectedGame.getGenres().contains(console)){
            entityManager.getTransaction().begin();
            selectedGame.addConsole(console);
            console.addGame(selectedGame);
            entityManager.getTransaction().commit();
        }
    }

    public void deleteConsoleFromGame(Game selectedGame, Console console) {
        entityManager.getTransaction().begin();
        selectedGame.deleteConsole(console);
        entityManager.getTransaction().commit();
    }

    public void deleteGenreFromGame(Game selectedGame, Genre genre) {
        entityManager.getTransaction().begin();
        selectedGame.deleteGenre(genre);
        entityManager.getTransaction().commit();
    }
}
