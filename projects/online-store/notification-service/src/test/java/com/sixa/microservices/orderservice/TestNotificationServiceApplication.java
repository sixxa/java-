package com.sixa.microservices.orderservice;

import org.springframework.boot.SpringApplication;

public class TestNotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(NotificationServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}