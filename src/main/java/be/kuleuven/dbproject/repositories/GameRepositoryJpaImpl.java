package be.kuleuven.dbproject.repositories;

import javax.persistence.EntityManager;

public class GameRepositoryJpaImpl {
    private final EntityManager entityManager;

    public GameRepositoryJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
