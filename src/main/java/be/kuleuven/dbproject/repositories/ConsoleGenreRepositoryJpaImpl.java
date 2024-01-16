package be.kuleuven.dbproject.repositories;

import be.kuleuven.dbproject.domain.Console;
import be.kuleuven.dbproject.domain.Genre;

import javax.persistence.EntityManager;
import java.util.List;

public class ConsoleGenreRepositoryJpaImpl {

    private final EntityManager entityManager;

    public ConsoleGenreRepositoryJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    public String[] getAllConsoleNames(){
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Console.class);
        var root = query.from(Console.class);
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
        var root = query.from(Console.class);
        query.where(criteriaBuilder.equal(root.get("name"),s));
        return entityManager.createQuery(query).getSingleResult();
    }

    public Genre getGenre(String s){
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Genre.class);
        var root = query.from(Genre.class);
        query.where(criteriaBuilder.equal(root.get("description"),s));
        return entityManager.createQuery(query).getSingleResult();
    }
    public String[] getAllGenreNames() {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Genre.class);
        var root = query.from(Genre.class);
        var list = entityManager.createQuery(query).getResultList();
        String[] results = new String[list.size()];
        for(int i = 0; i< list.size(); i++){
            results[i] = list.get(i).getDescription();
        }
        return results;
    }
    public List<Genre> getAllGenres(){
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Genre.class);
        var root = query.from(Genre.class);
        return entityManager.createQuery(query).getResultList();
    }
    public List<Console> getAllConsoles() {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Console.class);
        var root = query.from(Console.class);
        return entityManager.createQuery(query).getResultList();
    }

    public void addConsole(Console c) {
        entityManager.getTransaction().begin();
        entityManager.persist(c);
        entityManager.getTransaction().commit();
    }

    public void addGenre(Genre g) {
        entityManager.getTransaction().begin();
        entityManager.persist(g);
        entityManager.getTransaction().commit();
    }

    public void changeConsole(String name,int year,Console c) {
        entityManager.getTransaction().begin();
        c.setName(name);
        c.setYear(year);
        entityManager.merge(c);
        entityManager.getTransaction().commit();
    }

    public void changeGenre(String description, Genre g) {
        entityManager.getTransaction().begin();
        g.setDescription(description);
        entityManager.merge(g);
        entityManager.getTransaction().commit();
    }

    public void deleteConsole(Console console) {
        entityManager.getTransaction().begin();
        entityManager.remove(console);
        entityManager.getTransaction().commit();
    }

    public void deleteGenre(Genre genre){
        entityManager.getTransaction().begin();
        entityManager.remove(genre);
        entityManager.getTransaction().commit();

    }
}
