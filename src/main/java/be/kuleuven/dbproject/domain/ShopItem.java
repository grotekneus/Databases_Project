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

    public ShopItem(){

    }
    public ShopItem(String name, float price, Museum museum, ItemType itemType) {
        this.name = name;
        this.price = price;
        this.museum=museum;
        this.itemType=itemType;
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

    public void setMuseum(Museum museum){this.museum=museum;}

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

    public Museum getMuseum() {
        return museum;
    }
}
