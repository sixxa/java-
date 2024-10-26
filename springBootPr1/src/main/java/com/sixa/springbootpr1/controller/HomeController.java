package com.sixa.springbootpr1.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping("/")
    public String greet() {
        return "welcome to sixa";
    }
    @RequestMapping("/about")
    public String about(){
        return "we dont teach we aducate";
    }
}
