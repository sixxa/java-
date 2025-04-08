package com.sixa.giveawayapp;

import org.springframework.boot.SpringApplication;

public class TestGiveAwayAppApplication {

    public static void main(String[] args) {
        SpringApplication.from(GiveAwayAppApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
