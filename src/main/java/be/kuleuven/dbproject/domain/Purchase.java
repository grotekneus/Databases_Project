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

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public Purchase(){}

    public Purchase(Customer customer, ItemType itemType, int itemID) {
        this.customer = customer;
        this.itemType = itemType;
        this.itemID = itemID;
    }

    public ItemType getItemType() {
        return itemType;
    }


    public Customer getCustomer() {
        return customer;
    }

    public int getItemID() {
        return itemID;
    }

    public int getPurchaseID() {
        return purchaseID;
    }
}
