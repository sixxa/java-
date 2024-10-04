package org.example;

import java.util.ArrayList;
import java.util.List;

public class AlienRepository {
    List<Alien> aliens;

    public AlienRepository() {
        aliens = new ArrayList<Alien>();
        Alien a1 = new Alien();
        a1.setId(1);  // Set the id for each alien
        a1.setName("Toka");
        a1.setPoints(100);

        Alien a2 = new Alien();
        a2.setId(2);  // Set the id for each alien
        a2.setName("Navin");
        a2.setPoints(70);

        aliens.add(a1);
        aliens.add(a2);
    }

    public List<Alien> getAliens() {
        return aliens;
    }

    public Alien getAlien(int id) {
        for (Alien a : aliens) {
            if (a.getId() == id) {  // Use getId() instead of getid()
                return a;
            }
        }
        return null;  // Move return statement outside the loop
    }
}
