package be.kuleuven.dbproject.repositories;

import be.kuleuven.dbproject.domain.Console;
import be.kuleuven.dbproject.domain.Museum;

import javax.persistence.EntityManager;

public class MuseumRepositoryJpaImpl {
    private final EntityManager entityManager;

    public MuseumRepositoryJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;

    }

    public String[] getAllMuseumNames() {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Museum.class);
        var root = query.from(Museum.class); //blijkbaar selecteerd hij default de hele klasse
        var list = entityManager.createQuery(query).getResultList();
        String[] results = new String[list.size()];
        for(int i = 0; i< list.size(); i++){
            results[i] = list.get(i).getAddress();
        }
        return results;
    }

    public Museum getMuseum(String s) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Museum.class);
        var root = query.from(Museum.class); //blijkbaar selecteerd hij default de hele klasse
        query.where(criteriaBuilder.equal(root.get("address"),s));
        return entityManager.createQuery(query).getSingleResult();
    }
}
