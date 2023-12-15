package be.kuleuven.dbproject.repositories;


import be.kuleuven.dbproject.domain.ShopItem;

import javax.persistence.EntityManager;
import java.util.List;

public class ShopItemRepositoryJpaImpl {
    private final EntityManager entityManager;

    public ShopItemRepositoryJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<ShopItem> getShopItems(){
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(ShopItem.class);
        var root = query.from(ShopItem.class); //blijkbaar selecteerd hij default de hele klasse
        return entityManager.createQuery(query).getResultList();
    }
    public void addShopItem(ShopItem s) {
        entityManager.getTransaction().begin();
        entityManager.persist(s);
        entityManager.getTransaction().commit();
    }

}
