package be.kuleuven.dbproject.repositories;

import be.kuleuven.dbproject.domain.Museum;

import javax.persistence.EntityManager;
import java.util.List;

public class MuseumRepositoryJpaImpl {
    private final EntityManager entityManager;

    public MuseumRepositoryJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;

    }

    public String[] getAllMuseumAdresses() {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Museum.class);
        var root = query.from(Museum.class);
        var list = entityManager.createQuery(query).getResultList();
        String[] results = new String[list.size()];
        for(int i = 0; i< list.size(); i++){
            results[i] = list.get(i).getAddress();
        }
        return results;
    }
    public List<Museum> getMuseums(){
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Museum.class);
        var root = query.from(Museum.class);
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

    public Museum getMuseumByAddress(String address) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Museum.class);
        var root = query.from(Museum.class);
        query.where(criteriaBuilder.equal(root.get("address"), address));

        try {
            return entityManager.createQuery(query).getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            return null;
        }
    }
}
