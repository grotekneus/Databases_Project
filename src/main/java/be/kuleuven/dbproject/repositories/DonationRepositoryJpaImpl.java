package be.kuleuven.dbproject.repositories;

import be.kuleuven.dbproject.domain.Customer;
import be.kuleuven.dbproject.domain.Donation;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Join;
import java.util.List;

public class DonationRepositoryJpaImpl {
    private final EntityManager entityManager;

    public DonationRepositoryJpaImpl(EntityManager entityManager) {this.entityManager = entityManager;}

    public List<Donation> getDonations(){
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Donation.class);
        var root = query.from(Donation.class);
        return entityManager.createQuery(query).getResultList();
    }
    public void addDonation(Donation d) {
        entityManager.getTransaction().begin();
        entityManager.persist(d);
        entityManager.getTransaction().commit();
    }

    public List<Donation> getDonationsByCustomer(Customer customer) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Donation.class);
        var root = query.from(Donation.class);
        Join<Donation, Customer> customerJoin = root.join("customer");
        query.select(root).where(criteriaBuilder.equal(customerJoin.get("customerID"), customer.getCustomerID()));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Donation> getDonationsAbovePrice(float price) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Donation.class);
        var root = query.from(Donation.class);
        query.select(root).where(criteriaBuilder.greaterThanOrEqualTo(root.get("moneyDonated"), price));
        return entityManager.createQuery(query).getResultList();
    }

    public void changeDonation(Donation donation, int value) {
        entityManager.getTransaction().begin();
        donation.setMoneyDonated(value);
        entityManager.merge(donation);
        entityManager.getTransaction().commit();
    }
}