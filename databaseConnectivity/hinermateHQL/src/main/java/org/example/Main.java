package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {

        Configuration config = new Configuration().configure().addAnnotatedClass(Student.class);
        ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
        SessionFactory sf = config.buildSessionFactory(registry);
        Session session = sf.openSession();

        session.beginTransaction();

        Query query = session.createQuery("from Student");
        List<Student> students = query.list();

        for (Student student : students) {
            System.out.println(student);
        }

    /*
        Random rand = new Random();

        for (int i = 1; i <= 50; i++) {
            Student s = new Student();
            s.setRollno(i);
            s.setName("name" + i);
            s.setMarks(rand.nextInt(100));
            session.save(s);
        }
    */
        session.getTransaction().commit();
    }
}