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
}
