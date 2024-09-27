package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Main {
    public static void main(String[] args) {

        Laptop laptop = new Laptop();
        laptop.setlid(101);
        laptop.setlname("Dell");

        Student s = new Student();
        s.setRollno(1);
        s.setName("Navin");
        s.setMarks(50);
        laptop.setStudent(s);
        s.getLaptop().add(laptop);


        Configuration config = new Configuration().configure().addAnnotatedClass(Student.class).addAnnotatedClass(Laptop.class);
        ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        SessionFactory sf = config.buildSessionFactory(registry);
        Session session = sf.openSession();

        session.beginTransaction();
        session.save(laptop);
        session.save(s);

        session.getTransaction().commit();
    }
}
