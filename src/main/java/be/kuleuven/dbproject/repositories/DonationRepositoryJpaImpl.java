package be.kuleuven.dbproject.repositories;

import be.kuleuven.dbproject.domain.Donation;

import javax.persistence.EntityManager;
import java.util.List;

public class DonationRepositoryJpaImpl {
    private final EntityManager entityManager;

    public DonationRepositoryJpaImpl(EntityManager entityManager) {this.entityManager = entityManager;}

    public List<Donation> getDonations(){
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Donation.class);
        var root = query.from(Donation.class); //blijkbaar selecteerd hij default de hele klasse
        return entityManager.createQuery(query).getResultList();
    }
    public void addDonation(Donation d) {
        entityManager.getTransaction().begin();
        entityManager.persist(d);
        entityManager.getTransaction().commit();
    }
}


/*
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
 */
