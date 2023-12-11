package be.kuleuven.dbproject.repositories;

import javax.persistence.EntityManager;

public class ShopItemRepositoryJpaImpl {
    private final EntityManager entityManager;

    public ShopItemRepositoryJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
