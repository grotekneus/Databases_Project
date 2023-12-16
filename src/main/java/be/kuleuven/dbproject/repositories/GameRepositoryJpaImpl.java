package be.kuleuven.dbproject.repositories;

import be.kuleuven.dbproject.domain.*;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class GameRepositoryJpaImpl {
    private final EntityManager entityManager;

    public GameRepositoryJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        Console c = new Console("PS3", LocalDate.now());
        Genre g = new Genre("Fantasy");
        entityManager.getTransaction().begin();
        entityManager.persist(c);
        entityManager.persist(g);
        entityManager.getTransaction().commit();
    }

    public String[] getAllConsoleNames(){
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Console.class);
        var root = query.from(Console.class); //blijkbaar selecteerd hij default de hele klasse
        var list = entityManager.createQuery(query).getResultList();
        String[] results = new String[list.size()];
        for(int i = 0; i< list.size(); i++){
            results[i] = list.get(i).getName();
        }
        return results;
    }
    public Console getConsole(String s){
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Console.class);
        var root = query.from(Console.class); //blijkbaar selecteerd hij default de hele klasse
        query.where(criteriaBuilder.equal(root.get("name"),s));
        return entityManager.createQuery(query).getSingleResult();
    }

    public Genre getGenre(String s){
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Genre.class);
        var root = query.from(Genre.class); //blijkbaar selecteerd hij default de hele klasse
        query.where(criteriaBuilder.equal(root.get("description"),s));
        return entityManager.createQuery(query).getSingleResult();
    }

    public List<Game> getAllGames() {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Game.class);
        var root = query.from(Game.class); //blijkbaar selecteerd hij default de hele klasse
        return entityManager.createQuery(query).getResultList();
    }

    public List<GameInstance> getAllGameInstances(int GameID) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(GameInstance.class);
        var root = query.from(GameInstance.class); //blijkbaar selecteerd hij default de hele klasse
        query.where(criteriaBuilder.equal(root.get("gameID"),GameID));
        return entityManager.createQuery(query).getResultList();
    }

    public List<GameInstance> getAllGameInstancesBasedOnMuseum(int MuseumID) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(GameInstance.class);
        var root = query.from(GameInstance.class); //blijkbaar selecteerd hij default de hele klasse
        query.where(criteriaBuilder.equal(root.get("museumID"),MuseumID));
        return entityManager.createQuery(query).getResultList();
    }

    public void addGame(Game g) {

        entityManager.getTransaction().begin();
        entityManager.persist(g);
        entityManager.getTransaction().commit();
    }

    public String[] getAllGenreNames() {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Genre.class);
        var root = query.from(Genre.class); //blijkbaar selecteerd hij default de hele klasse
        var list = entityManager.createQuery(query).getResultList();
        String[] results = new String[list.size()];
        for(int i = 0; i< list.size(); i++){
            results[i] = list.get(i).getDescription();
        }
        return results;
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

    public void changeGameInstanceMuseum(int museumID, GameInstance selectedGInstance) {
        entityManager.getTransaction().begin();
        selectedGInstance.setMuseumID(museumID);
        entityManager.merge(selectedGInstance);
        entityManager.getTransaction().commit();
    }
}
