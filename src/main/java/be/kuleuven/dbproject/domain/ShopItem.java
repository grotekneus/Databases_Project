package be.kuleuven.dbproject.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ShopItem {
    @Column
    private String name;
    @Column
    private ItemType itemType;
    @Column
    private float price;
    @Column
    @Id
    @GeneratedValue
    private int itemID;
    @Column
    private int museumID;

    public ShopItem(String name, float price) {
        this.name = name;
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public float getPrice() {
        return price;
    }

    public int getItemID() {
        return itemID;
    }

    public int getMuseumID() {
        return museumID;
    }
}
