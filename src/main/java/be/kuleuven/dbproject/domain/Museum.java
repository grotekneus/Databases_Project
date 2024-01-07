package be.kuleuven.dbproject.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Museum {
    @Column
    private String address;
    @Column
    private int visitors;
    @Column
    @Id
    @GeneratedValue
    private int museumID;
    @Column
    private float revenue;
    @Column
    private String country;

    public Museum(){

    }
    public Museum(String address, int visitors, float revenue, String country) {
        this.address = address;
        this.visitors = visitors;
        this.revenue = revenue;
        this.country = country;
    }
    public void incrementVisitors(){
        visitors+=1;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getVisitors() {
        return visitors;
    }

    public void setVisitors(int visitors) {
        this.visitors = visitors;
    }

    public float getRevenue() {
        return revenue;
    }

    public int getMuseumID() {
        return museumID;
    }

    public void setRevenue(float revenue) {
        this.revenue = revenue;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
