package be.kuleuven.dbproject.domain;

import javax.persistence.*;

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
    @ManyToOne
    @JoinColumn

    private Museum museum;

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

    public Museum getMuseumID() {
        return museum;
    }
}
