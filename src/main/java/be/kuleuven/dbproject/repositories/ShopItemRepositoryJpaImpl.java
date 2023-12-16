package be.kuleuven.dbproject.repositories;


import be.kuleuven.dbproject.domain.Museum;
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

    public void updateShopItem(ShopItem shopItem) {
        entityManager.getTransaction().begin();
        entityManager.merge(shopItem);
        entityManager.getTransaction().commit();
    }

    public ShopItem getShopItemById(int itemId) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(ShopItem.class);
        var root = query.from(ShopItem.class);
        query.where(criteriaBuilder.equal(root.get("itemID"), itemId));

        try {
            return entityManager.createQuery(query).getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            // Handle the case where no ShopItem with the given ID is found
            return null;
        }
    }
    public void addShopItem(ShopItem s) {
        entityManager.getTransaction().begin();
        entityManager.persist(s);
        entityManager.getTransaction().commit();
    }

}
