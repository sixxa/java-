package org.example.springjdbc;

import org.example.springjdbc.model.Alien;
import org.example.springjdbc.rep.AlienRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;

@SpringBootApplication
public class SpringJdbcApplication {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(SpringJdbcApplication.class, args);

        Alien alien1 = context.getBean(Alien.class);
        alien1.setId(111);
        alien1.setName("Toka");
        alien1.setTech("java");

        AlienRepo repo = context.getBean(AlienRepo.class);
        repo.save(alien1);

        System.out.println(repo.findAll());
    }

}
