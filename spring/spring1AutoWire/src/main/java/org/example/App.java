package org.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class App {
    public static void main(String[] args) {

        ApplicationContext context = new FileSystemXmlApplicationContext("D:\\java-\\spring0\\spring.xml");


        Alien obj1 = context.getBean(Alien.class);
        obj1.code();
        System.out.println(obj1.getAge());

    }
}