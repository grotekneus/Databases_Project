package be.kuleuven.dbproject.repositories;

import javax.persistence.EntityManager;

public class MuseumRepositoryJpaImpl {
    private final EntityManager entityManager;

    public MuseumRepositoryJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
