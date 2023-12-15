package be.kuleuven.dbproject.repositories;

import be.kuleuven.dbproject.domain.Customer;
import be.kuleuven.dbproject.domain.Museum;

import javax.persistence.EntityManager;
import java.util.List;

public class MuseumRepositoryJpaImpl {
    private final EntityManager entityManager;

    public MuseumRepositoryJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public List<Museum> getMuseums(){
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Museum.class);
        var root = query.from(Museum.class); //blijkbaar selecteerd hij default de hele klasse
        return entityManager.createQuery(query).getResultList();
    }
    public void addMuseum(Museum m) {
        entityManager.getTransaction().begin();
        entityManager.persist(m);
        entityManager.getTransaction().commit();
    }
    public void updateMuseum(Museum museum) {
        entityManager.getTransaction().begin();
        entityManager.merge(museum);
        entityManager.getTransaction().commit();
    }

    public List<Museum> getMuseumsByAddress(String address) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Museum.class);
        var root = query.from(Museum.class);

        // Assuming 'address' is a field in your Museum entity
        query.where(criteriaBuilder.equal(root.get("address"), address));

        return entityManager.createQuery(query).getResultList();
    }
}
