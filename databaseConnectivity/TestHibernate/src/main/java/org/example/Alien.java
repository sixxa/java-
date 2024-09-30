package org.example;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "student")  // Ensure this matches the existing table name
public class Alien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Assuming ID is auto-generated
    private int userID;

    @Embedded  // Use @Embedded to store AlienName in the same table
    private AlienName userName;

    private String color;

    // Getters and Setters
    public int getUserID() {
        return userID;
    }

    public AlienName getUserName() {
        return userName;
    }

    public void setUserName(AlienName userName) {
        this.userName = userName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Alien{" +
                "userID=" + userID +
                ", userName=" + userName +  // Display AlienName details properly
                ", color='" + color + '\'' +
                '}';
    }
}
