package org.example;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;  // Import the Table annotation
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;



@Entity
@Table(name = "alien_table")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)

public class Alien {

    @Id
    private int aid;
    private String aname;
    private String color;

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public String getcolor() {
        return color;
    }

    public void setcolor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Alien{" +
                "aid=" + aid +
                ", aname='" + aname + '\'' +
                ", acolor='" + color + '\'' +
                '}';
    }
}
