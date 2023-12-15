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
    public List<Genre> getAllGenres(){
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Genre.class);
        var root = query.from(Genre.class); //blijkbaar selecteerd hij default de hele klasse
        return entityManager.createQuery(query).getResultList();
    }
    public List<Console> getAllConsoles() {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Console.class);
        var root = query.from(Console.class); //blijkbaar selecteerd hij default de hele klasse
        return entityManager.createQuery(query).getResultList();
    }
}
