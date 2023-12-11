package be.kuleuven.dbproject.domain;

import javax.persistence.*;

@Entity
public class Purchase {
    @ManyToOne
    @JoinColumn(name="customerId")
    private Customer customer;
    @Column
    private ItemType itemType;
    @Column
    private int itemID;
    @Column
    @Id
    @GeneratedValue
    private int purchaseID;

    public Purchase() {
    }

    public ItemType getItemType() {
        return itemType;
    }


    public Customer getCustomerID() {
        return customer;
    }

    public int getItemID() {
        return itemID;
    }

    public int getPurchaseID() {
        return purchaseID;
    }
}
