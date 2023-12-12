package be.kuleuven.dbproject.repositories;

import be.kuleuven.dbproject.domain.Console;
import be.kuleuven.dbproject.domain.Customer;

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
}
