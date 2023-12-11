package be.kuleuven.dbproject.repositories;

import javax.persistence.EntityManager;

public class DonationRepositoryJpaImpl {
    private final EntityManager entityManager;

    public DonationRepositoryJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
