package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class AlienDAO {

    private static SessionFactory sessionFactory;

    // Initialize the SessionFactory in a static block
    static {
        try {
            // Load the configuration and build the SessionFactory
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Alien.class)
                    .addAnnotatedClass(AlienName.class)
                    .buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("SessionFactory creation failed");
        }
    }

    // Method to get the SessionFactory
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // Method to save Alien to the database
    public void saveAlien(Alien alien) {
        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) {
            // Begin transaction
            transaction = session.beginTransaction();

            // Save the Alien object
            session.save(alien);

            // Commit the transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // Method to get an Alien object from the database
    public Alien getAlien(int userID) {
        Alien alien = null;
        try (Session session = getSessionFactory().openSession()) {
            // Fetch the Alien object by userID
            alien = session.get(Alien.class, userID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return alien;
    }
}
