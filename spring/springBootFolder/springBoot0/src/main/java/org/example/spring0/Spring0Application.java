package org.example.spring0;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Spring0Application {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(Spring0Application.class, args);

        Alien obj = context.getBean(Alien.class);

        obj.code();

    }

}
