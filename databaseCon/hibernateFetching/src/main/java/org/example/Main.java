package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Collection;

public class Main {
    public static void main(String[] args) {

        Configuration config = new Configuration().configure().addAnnotatedClass(Alien.class).addAnnotatedClass(Laptop.class);
        ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        SessionFactory sf = config.buildSessionFactory(registry);
        Session session = sf.openSession();

        Alien a1 = session.get(Alien.class, 1);

        System.out.println(a1.getAname());

        // either fetch type eager or the version bellow

    //    Collection<Laptop> laps = a1.getLaps();
    //    for (Laptop l : laps) {
    //        System.out.println(l);
    //   }


        session.getTransaction().commit();
    }
}
