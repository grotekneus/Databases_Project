package be.kuleuven.dbproject.repositories;

import be.kuleuven.dbproject.domain.*;

import javax.persistence.EntityManager;
import java.util.List;

public class CustomerRepositoryJpaImpl {

    private final EntityManager entityManager;

    public CustomerRepositoryJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Customer> getCustomers(){
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Customer.class);
        var root = query.from(Customer.class); //blijkbaar selecteerd hij default de hele klasse
        return entityManager.createQuery(query).getResultList();
    }

    public void addCustomer(Customer c) {
        entityManager.getTransaction().begin();
        entityManager.persist(c);
        entityManager.getTransaction().commit();
    }

    public List<Loan> getLoans(Customer customer) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Loan.class);
        var root = query.from(Loan.class); //blijkbaar selecteerd hij default de hele klasse
        query.where(criteriaBuilder.equal(root.get("customer"), customer));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Donation> getDonations(Customer c) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Donation.class);
        var root = query.from(Donation.class); //blijkbaar selecteerd hij default de hele klasse
        query.where(criteriaBuilder.equal(root.get("customer"), c));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Purchase> getPurchases(Customer c) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Purchase.class);
        var root = query.from(Purchase.class); //blijkbaar selecteerd hij default de hele klasse
        query.where(criteriaBuilder.equal(root.get("customer"), c));
        return entityManager.createQuery(query).getResultList();
    }

    public void addLoan(Loan loan) {
    }
}
