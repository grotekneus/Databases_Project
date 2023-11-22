package be.kuleuven.dbproject.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Purchase {
    @Column
    private int customerID;
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


    public int getCustomerID() {
        return customerID;
    }

    public int getItemID() {
        return itemID;
    }

    public int getPurchaseID() {
        return purchaseID;
    }
}
