package be.kuleuven.dbproject;

import javax.persistence.EntityManager;

public class EntityManagerClass {

    private EntityManager entityManager;

    public EntityManagerClass(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
