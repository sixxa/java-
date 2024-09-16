package org.example;

public class Main {
    public static void main(String[] args) {
        AlienDAO alienDAO = new AlienDAO();
        Alien ucxo = new Alien();

        AlienName ucxoName = new AlienName();
        ucxoName.setfName("pavle");
        ucxoName.setlName("petre");
        ucxoName.setmName("metreveli");

        ucxo.setColor("purple");
        ucxo.setUserName(ucxoName);

        alienDAO.saveAlien(ucxo);

        System.out.println("Alien saved: " + ucxo);



        // Fetch the Alien object with userID 1 (you can change the ID to fetch different objects)
        Alien Toka = alienDAO.getAlien(1);

        // Print the fetched object
        if (Toka != null) {
            System.out.println(Toka);
        } else {
            System.out.println("Alien not found!");
        }
    }
}
