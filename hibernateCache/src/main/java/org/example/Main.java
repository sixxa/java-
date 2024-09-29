package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Main {
    public static void main(String[] args) {

        Alien a = null;

        // Setup Hibernate configuration and registry
        Configuration config = new Configuration().configure().addAnnotatedClass(Alien.class);
        ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        SessionFactory sf = config.buildSessionFactory(registry);

        // First session
        Session session1 = sf.openSession();
        session1.beginTransaction(); // Start the transaction
        a = session1.get(Alien.class, 101); // Retrieve Alien with ID 101
        System.out.println("First retrieval:");
        System.out.println(a);
        session1.getTransaction().commit(); // Commit the transaction
        session1.close(); // Close the first session

        // Second session
        Session session2 = sf.openSession();
        session2.beginTransaction(); // Start the transaction
        a = session2.get(Alien.class, 101); // Retrieve Alien again
        System.out.println("Second retrieval:");
        System.out.println(a);
        session2.getTransaction().commit(); // Commit the transaction
        session2.close(); // Close the second session
    }
}
