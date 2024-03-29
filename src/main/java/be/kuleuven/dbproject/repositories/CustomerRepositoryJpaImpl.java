package be.kuleuven.dbproject.repositories;

import be.kuleuven.dbproject.domain.*;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class CustomerRepositoryJpaImpl {

    private final EntityManager entityManager;

    public CustomerRepositoryJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Customer> getAllCustomers(){
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Customer.class);
        var root = query.from(Customer.class);
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
        var root = query.from(Loan.class);
        query.where(criteriaBuilder.equal(root.get("customer"), customer));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Donation> getDonations(Customer c) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Donation.class);
        var root = query.from(Donation.class);
        query.where(criteriaBuilder.equal(root.get("customer"), c));
        return entityManager.createQuery(query).getResultList();
    }

    public List<Purchase> getPurchases(Customer c) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Purchase.class);
        var root = query.from(Purchase.class);
        query.where(criteriaBuilder.equal(root.get("customer"), c));
        return entityManager.createQuery(query).getResultList();
    }

    public Customer getCustomerById(int customerId) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Customer.class);
        var root = query.from(Customer.class);
        query.where(criteriaBuilder.equal(root.get("customerID"), customerId));
        return entityManager.createQuery(query).getSingleResult();
    }

    public void addLoan(Loan loan) {
        entityManager.getTransaction().begin();
        entityManager.persist(loan);
        entityManager.getTransaction().commit();
    }

    public void changeCustomerProperties(Customer selectedCustomer, String address, String email, String fullName) {
        entityManager.getTransaction().begin();
        selectedCustomer.setAddress(address);
        selectedCustomer.setEmail(email);
        selectedCustomer.setFullName(fullName);
        entityManager.merge(selectedCustomer);
        entityManager.getTransaction().commit();
    }

    public Customer getCustomer(String name){
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var query = criteriaBuilder.createQuery(Customer.class);
        var root = query.from(Customer.class);
        query.where(criteriaBuilder.equal(root.get("name"), name));
        return entityManager.createQuery(query).getSingleResult();
    }


    public void addPurchase(Purchase purchase) {
        entityManager.getTransaction().begin();
        entityManager.persist(purchase);
        entityManager.getTransaction().commit();
    }

    public void deleteLoan(Loan selectedLoan) {
        entityManager.getTransaction().begin();
        entityManager.remove(selectedLoan);
        entityManager.getTransaction().commit();
    }
}
